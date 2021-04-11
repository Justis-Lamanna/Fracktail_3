package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.magic.platform.MultiMessage;
import com.github.lucbui.fracktail3.magic.platform.NonePerson;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.modules.games.Action;
import com.github.lucbui.fracktail3.modules.games.ActionLegality;
import com.github.lucbui.fracktail3.modules.games.checkers.action.MoveAction;
import com.github.lucbui.fracktail3.modules.games.standard.BoardDisplay;
import com.github.lucbui.fracktail3.modules.games.standard.action.InTurnAction;
import com.github.lucbui.fracktail3.modules.games.standard.field.Position;
import com.github.lucbui.fracktail3.spring.command.annotation.*;
import com.github.lucbui.fracktail3.spring.schedule.annotation.InjectPlatform;
import discord4j.common.util.Snowflake;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CheckersManager {
    private final Map<String, Session> games = new HashMap<>();

    public synchronized Session startGame(Person red, Person black) {
        String id = generateId();
        Session session = new Session(id, new Checkers(), red, black);
        games.put(id, session);
        session.start();
        return session;
    }

    private String generateId() {
        String rid;
        do {
            rid = RandomStringUtils.randomAlphanumeric(6);
        } while (games.containsKey(rid));
        return rid;
    }

    public boolean endGame(String id) {
        return games.remove(id) != null;
    }

    public Optional<Session> getGame(String id) {
        return Optional.ofNullable(games.get(id));
    }

    public List<Session> getGame(Person player) {
        return games.values().stream()
                .filter(session -> session.isPlayer(player))
                .collect(Collectors.toList());
    }

    @Command
    @Usage("Play Checkers! Commands are as follows:\n" +
            "\t!checkers start <@opponent> - Start a game with an opponent.\n" +
            "\t!checkers play <id?> <position start>:<position end> - Move a piece. Positions are in the form of Column/Row pairs (example: A1)\n" +
            "\t!checkers watch <id> - Spectate on a match")
    public Mono<String> checkers(@InjectPlatform Platform platform,
                                 @InjectPerson Person sender,
                                 @Parameter(0) String subCommand,
                                 @ParameterRange(lower = 1) String[] parameters) {
        if(StringUtils.equalsIgnoreCase(subCommand, "start")) {
            if(parameters.length == 0) {
                return Mono.just("Usage: !checkers start <@opponent>");
            } else {
                return doStartGame(platform, sender, parameters[0]);
            }
        } else if(StringUtils.equalsIgnoreCase(subCommand, "watch")) {
            if(parameters.length == 0) {
                return Mono.just("Usage: !checkers watch <id>");
            } else {
                return doWatchGame(sender, parameters[1]);
            }
        } else if(StringUtils.equalsIgnoreCase(subCommand, "play")) {
            if(parameters.length == 1) {
                return doPlayWithImplicitId(sender, parameters[0]);
            } else if(parameters.length == 2) {
                return doPlayWithExplicitId(sender, parameters[0], parameters[1]);
            } else {
                return Mono.just("Usage: !checkers play <id?> <move>. Move format is '[position of piece]:[place to move]'");
            }
        } else {
            return Mono.just("Usage: Heck u");
        }
    }

    private Mono<String> doPlayWithExplicitId(Person sender, String matchId, String play) {
        return getGame(matchId)
                .map(session -> doPlay(session, sender, play))
                .orElse(Mono.just("That game does not exist. Please check the ID."));
    }

    private Mono<String> doPlayWithImplicitId(Person sender, String play) {
        List<Session> sessions = getGame(sender);
        if(sessions.isEmpty()) {
            return Mono.just("You have no games right now. You can start one with !checkers start <@opponent>");
        } else if(sessions.size() > 1) {
            return Mono.just("You have multiple games playing. Please specify the ID of the match to play.");
        } else {
            return doPlay(sessions.get(0), sender, play);
        }
    }

    private Mono<String> doPlay(Session session, Person sender, String play) {
        return session.getColor(sender)
                .map(player -> {
                    try { return doAction(parseAction(session.game.getGameField(), player, play), session.game); }
                    catch (IllegalArgumentException ex) { return Mono.just(ex.getMessage()); }
                })
                .orElse(Mono.just("You are not participating in this game. You cannot make a move."));
    }

    private Mono<String> doWatchGame(Person sender, String matchId) {
        return getGame(matchId)
                .map(s -> {
                    if(s.isPlayer(sender)) {
                        return Mono.just("Can't watch your own match!");
                    } else {
                        s.addSpectator(sender);
                        return Mono.just("Now spectating match " + s.id);
                    }
                })
                .orElse(Mono.just("No match with ID " + matchId));
    }

    private Mono<String> doStartGame(Platform platform, Person sender, String opponentMention) {
        try {
            Snowflake opponent = FormatUtils.fromUserMention(opponentMention);
            return platform.getPerson(opponent.asString())
                    .flatMap(opp -> {
                        if(opp == NonePerson.INSTANCE) {
                            return Mono.just("I have no idea who that person is, unfortunately.");
                        } else if(opp.isBot()) {
                            return Mono.just("You can't play checkers with a bot.");
                        } else if(opp.equals(sender)) {
                            return Mono.just("You can't play checkers with yourself!");
                        } else {
                            //TODO: Consent
                            Session s = startGame(sender, opp);
                            return Mono.just("Started a new game with ID: " + s.id);
                        }
                    });
        } catch (NumberFormatException e) {
            return Mono.just("Usage: !checkers start <@opponent>");
        }
    }

    private Action<Checkerboard> parseAction(Checkerboard board, Color player, String playStr) {
        String[] fromTo = playStr.split(":");
        if(fromTo.length != 2) {
            throw new IllegalArgumentException("Unknown format");
        }
        Position from = Position.parse(fromTo[0]);
        Position to = Position.parse(fromTo[1]);
        Piece piece = board.getPiece(from)
                .orElseThrow(() -> new IllegalArgumentException("No piece at position " + fromTo[0]));
        return new MoveAction(player, piece, to);
    }

    private Mono<String> doAction(Action<Checkerboard> action, Checkers game) {
        ActionLegality legality = game.canPerformAction(action);
        if(legality.isLegal()) {
            game.performAction(action);
            return Mono.empty();
        } else {
            return Mono.just("Attempted play is illegal: " + legality.getReason());
        }
    }

    @Data
    public static class Session {
        final String id;
        final Checkers game;
        final Person red;
        final Person black;
        final List<Person> spectators = new ArrayList<>();

        public boolean isPlayer(Person person) {
            return red.equals(person) || black.equals(person);
        }

        public Optional<Color> getColor(Person person) {
            if(red.equals(person)) {
                return Optional.of(Color.RED);
            } else if(black.equals(person)) {
                return Optional.of(Color.BLACK);
            } else {
                return Optional.empty();
            }
        }

        public Person getPlayer(Color color) {
            return color == Color.RED ? red : black;
        }

        public void start() {
            red.getPrivateChannel()
                    .flatMap(place -> place.sendMessage("You start.\n" + BoardDisplay.display(game.getGameField())))
                    .subscribe();

            black.getPrivateChannel()
                    .flatMap(place -> place.sendMessage("Red goes first. I'll let you know when it is your turn."))
                    .subscribe();

            game.actionsFeed()
                    .flatMap(turn -> {
                        //Determine the player
                        InTurnAction<Checkerboard, Color> ma = (InTurnAction<Checkerboard, Color>) turn.getAction();
                        Color player = ma.getPlayer();
                        String toRedMessage;
                        String toBlackMessage;
                        if(player == Color.RED) {
                            toRedMessage = "You played. " + black.getName() + "'s turn!";
                            toBlackMessage = red.getName() + " has played. Your turn!\n" + BoardDisplay.display(turn.getPostAction());
                        } else {
                            toRedMessage = black.getName() + " has played. Your turn!\n"  + BoardDisplay.display(turn.getPostAction());
                            toBlackMessage = "You played. " + red.getName() + "'s turn!";
                        }
                        return Flux.merge(
                                red.getPrivateChannel().flatMap(place -> place.sendMessage(toRedMessage)),
                                black.getPrivateChannel().flatMap(place -> place.sendMessage(toBlackMessage))
                        )
                        .collectList()
                        .map(MultiMessage::new);
                    })
                    .doOnComplete(() -> {
                        String toRedMessage;
                        String toBlackMessage;
                        Map<Color, Long> standings = game.getGameField().getStandings();
                        if(standings.get(Color.RED) == 0) {
                            toRedMessage = "You lost. Better luck next time!";
                            toBlackMessage = "You won, congrats!";
                        } else if(standings.get(Color.BLACK) == 0) {
                            toBlackMessage = "You lost. Better luck next time!";
                            toRedMessage = "You won, congrats!";
                        } else {
                            toBlackMessage = toRedMessage = "It was a draw. Better luck next time!";
                        }

                        red.getPrivateChannel().flatMap(place -> place.sendMessage(toRedMessage)).subscribe();
                        black.getPrivateChannel().flatMap(place -> place.sendMessage(toBlackMessage)).subscribe();
                    })
                    .subscribe();
        }

        public void addSpectator(Person spectator) {
            game.actionsFeed()
                    .flatMap(turn -> {
                        InTurnAction<Checkerboard, Color> ma = (InTurnAction<Checkerboard, Color>) turn.getAction();
                        Color player = ma.getPlayer();
                        String message = getPlayer(player).getName() + " played.";
                        return spectator.getPrivateChannel().flatMap(place -> place.sendMessage(message +
                                "\n" + BoardDisplay.display(turn.getPostAction())));
                    })
                    .doOnComplete(() -> {
                        Map<Color, Long> standings = game.getGameField().getStandings();
                        if(standings.get(Color.RED) == 0) {
                            spectator.getPrivateChannel().flatMap(place -> place.sendMessage("Black wins!")).subscribe();
                        } else if(standings.get(Color.BLACK) == 0) {
                            spectator.getPrivateChannel().flatMap(place -> place.sendMessage("Red wins!")).subscribe();
                        } else {
                            spectator.getPrivateChannel().flatMap(place -> place.sendMessage("It ended in a draw.")).subscribe();
                        }
                    })
                    .subscribe();

            this.spectators.add(spectator);
        }
    }
}

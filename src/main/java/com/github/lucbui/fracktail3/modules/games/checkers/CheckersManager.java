package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.discord.context.DiscordBotPerson;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.magic.platform.NonePerson;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.command.annotation.*;
import com.github.lucbui.fracktail3.spring.schedule.annotation.InjectPlatform;
import discord4j.common.util.Snowflake;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
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
    @Usage("Play Checkers!")
    public Mono<String> checkers(@InjectPlatform Platform platform,
                                 @InjectPerson Person sender,
                                 @Parameter(0) String subCommand,
                                 @ParameterRange(lower = 1) String[] parameters) {
        if(StringUtils.equalsIgnoreCase(subCommand, "start")) {
            if(parameters.length == 0) {
                return Mono.just("Usage: !checkers start <@opponent>");
            } else {
                try {
                    Snowflake opponent = FormatUtils.fromUserMention(parameters[0]);
                    return platform.getPerson(opponent.asString())
                            .flatMap(opp -> {
                                if(opp == NonePerson.INSTANCE) {
                                    return Mono.just("You can't play checkers with an unknown person.");
                                } else if(opp instanceof DiscordBotPerson) {
                                    return Mono.just("You can't play checkers with a bot.");
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
        } else if(StringUtils.equalsIgnoreCase(subCommand, "watch")) {
            if(parameters.length == 0) {
                return Mono.just("Usage: !checkers watch <match id>");
            } else {
                return getGame(parameters[1])
                        .map(s -> {
                            if(s.isPlayer(sender)) {
                                return Mono.just("Can't watch your own match!");
                            } else {
                                s.getSpectators().add(sender);
                                return Mono.just("Now spectating match " + s.id);
                            }
                        })
                        .orElse(Mono.just("No match with ID " + parameters[1]));
            }
        } else if(StringUtils.equalsIgnoreCase(subCommand, "play")) {
            return Mono.just("Sorry I can't do that yet");
        } else {
            return Mono.just("Usage: Heck u");
        }
        //checkers start [opponent]
        //checkers play [id] [round]
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
    }
}

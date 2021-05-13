package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.CommandType;
import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.*;
import com.github.lucbui.fracktail3.discord.context.slash.DiscordSlashPlace;
import com.github.lucbui.fracktail3.discord.exception.CancelHookException;
import com.github.lucbui.fracktail3.discord.util.Disposables;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.*;
import com.github.lucbui.fracktail3.magic.platform.context.BasicCommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.ParameterParser;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

/**
 * A singleton which represents the Discord platform
 *
 * Person IDs can be specified in a URN-like format, shown below:
 * - user:[user id] - Retrieve this person by their user ID.
 * - member:[guild id]:[user id] - Retrieve this person as a member of the guild.
 * - role:[guild id]:[role id] - Retrieve a person that multiplexes to all people of a specific role.
 * - role:[guild id]:* - Retrieve a person that multiplexes to all people in the server (@everyone).
 *
 * Place IDs can be specified in a similar format, shown below:
 * - guild:[guild id] - Retrieve a place that multiplexes to all channels in the server (note: this won't send a message in every channel, only the system channel)
 * - guild:* - Retrieve a place that multiplexes to everywhere the bot is (note: this won't send a message in every channel, only the system channel)
 * - channel:[channel id] - Retrieve a place as a channel
 */
@Component
public class DiscordPlatform implements Platform, HealthIndicator, InfoContributor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatform.class);
    private static final String EVERYTHING_ID = "*";
    private static final String MULTI_ID_DELIMITER = ";";
    private static final String URN_DELIMITER = ":";
    private static final String COMMAND_FEED = "_command-feed";

    @Autowired
    private DiscordConfiguration configuration;

    @Autowired
    private ParameterParser parameterParser;

    private final Map<String, Disposable> subscriptionMap = new Disposables<>();
    private GatewayDiscordClient gateway;

    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public DiscordConfiguration getConfiguration() {
        return configuration;
    }

    public Set<String> getHooks() {
        return subscriptionMap.keySet();
    }

    @PreDestroy
    public void onDestroy() {
        //Clean up all subscriptions when we are done
        subscriptionMap.clear();
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(gateway != null) {
            return Mono.error(new BotConfigurationException("Attempted to start bot on Discord, but it was already running"));
        }

        DiscordClient discordClient =
                DiscordClientBuilder.create(configuration.getToken()).build();

        LOGGER.debug("Starting bot on Discord");
        gateway = discordClient.login().block();

        if(gateway == null) {
            throw new BotConfigurationException("Gateway was null");
        }

        //Configure custom hooks
        configuration.getHooks().forEach(hook -> registerHook(hook.getId(), hook.getHook()));

        //Set presence properly
        LOGGER.debug("Setting initial presence {}", configuration.getInitialPresence());
        gateway.updatePresence(configuration.getInitialPresence()).block();

        LOGGER.debug("Starting in {} mode", configuration.getCommandType());
        if (configuration.getCommandType() == CommandType.SLASH ) {
            configureBotForSlashCommand(bot);
        } else if(configuration.getCommandType() == CommandType.LEGACY) {
            configureBotForLegacyCommand(bot);
        } else {
            configureBotForSlashCommand(bot);
            configureBotForLegacyCommand(bot);
        }

        return gateway.onDisconnect().thenReturn(true);
    }

    private void configureBotForLegacyCommand(Bot bot) {
        Disposable d = getPlaceByEverywhere()
                .flatMapMany(Place::getMessageFeed)
                .filter(msg -> !msg.getSender().equals(NonePerson.INSTANCE))
                .filter(msg -> !msg.getSender().isBot())
                .filter(message -> message.getContent().startsWith(configuration.getPrefix()))
                .flatMap(message -> {
                    return Flux.fromIterable(bot.getSpec().getCommandList().getCommands())
                            .flatMap(c -> Flux.fromIterable(c.getNames()).map(name -> Tuples.of(name, c)))
                            .filter(t -> message.getContent().startsWith(configuration.getPrefix() + t.getT1()))
                            .map(t -> {
                                String cmdStr = t.getT1();
                                String pStr = StringUtils.removeStart(message.getContent(), configuration.getPrefix() + cmdStr)
                                        .trim();
                                return new DiscordCommandUseContext(
                                        bot,
                                        this,
                                        message,
                                        t.getT2(),
                                        parameterParser.parseParametersFromMessage(t.getT2(), pStr),
                                        configuration.getReplyStyle());
                            })
                            .filterWhen(CommandUseContext::canDoAction)
                            .next()
                            .flatMap(CommandUseContext::doAction)
                            .onErrorResume(Throwable.class, e -> {
                                LOGGER.error("Error during action", e);
                                return message.getOrigin()
                                        .flatMap(place ->
                                                place.sendMessage("I ran into an error there, sorry: " + e.getMessage() + ". Check the logs for more info."))
                                        .then();
                            });
                })
                .subscribe();
        subscriptionMap.put(COMMAND_FEED, d);
    }

    private void configureBotForSlashCommand(Bot bot) {
        Disposable d = gateway.on(InteractionCreateEvent.class)
                .flatMap(ice -> {
                    Interaction interaction = ice.getInteraction();
                    String command = ice.getCommandName();
                    return Flux.fromIterable(bot.getSpec().getCommandList())
                            .filter(c -> c.getNames().stream().anyMatch(name -> StringUtils.equalsIgnoreCase(command, name)))
                            .next()
                            .map(c -> {
                                DiscordPerson person = new DiscordPerson(interaction.getMember()
                                        .map(u -> (User)u)
                                        .orElse(interaction.getUser()));
//                                Holdover until this functionality works properly...
//                                Mono<Place> place = interaction.getChannel().map(tc -> new DiscordSlashPlace(tc, ice));
                                Mono<Place> place = gateway.getChannelById(interaction.getChannelId())
                                        .cast(MessageChannel.class)
                                        .map(mc -> new DiscordSlashPlace(mc, ice));
                                Parameters parameters = parseParamsFromICE(ice, c);
                                return new BasicCommandUseContext(bot, this, ice, person, place, c, parameters);
                            })
                            .flatMap(c -> ice.acknowledgeEphemeral().thenReturn(c))
                            .flatMap(c -> c.doAction()) //Compiler gets mad at me for using :: here??
                            .onErrorResume(Throwable.class, e -> {
                                e.printStackTrace();
                                return ice.getInteractionResponse()
                                        .createFollowupMessage("I ran into an error there, sorry: " + e.getMessage() + ". Check the logs for more info.")
                                        .then();
                            });
                })
                .subscribe();
        subscriptionMap.put(COMMAND_FEED, d);
    }

    private Parameters parseParamsFromICE(InteractionCreateEvent event, Command command) {
        if(command.getParameters().isEmpty()) {
            //Legacy, I guess.
            String[] parsed = event.getInteraction().getCommandInteraction().getOptions()
                    .stream()
                    .flatMap(acio -> acio.getValue().map(Stream::of).orElseGet(Stream::empty))
                    .map(ApplicationCommandInteractionOptionValue::getRaw)
                    .toArray(String[]::new);
            return new Parameters(String.join(" ", parsed), parsed);
        }
        ApplicationCommandInteraction acid = event.getInteraction().getCommandInteraction();
        int idx = 0;
        Object[] paramObjects = new Object[command.getParameters().size()];
        StringJoiner message = new StringJoiner(" ");
        for(Command.Parameter parameter : command.getParameters()) {
            String name = parameter.getName();
            Object value = acid.getOption(name)
                    .flatMap(acio -> {
                        switch (acio.getType()) {
                            case INTEGER: return acio.getValue().map(ApplicationCommandInteractionOptionValue::asLong);
                            case STRING: return acio.getValue().map(ApplicationCommandInteractionOptionValue::asString);
                            case BOOLEAN: return acio.getValue().map(ApplicationCommandInteractionOptionValue::asBoolean);
                            case ROLE:
                            case USER:
                            case CHANNEL: return acio.getValue().map(ApplicationCommandInteractionOptionValue::asSnowflake);
                            default: return Optional.empty();
                        }
                    })
                    .orElse(null);
            paramObjects[idx++] = value;
            acid.getOption(name)
                    .flatMap(ApplicationCommandInteractionOption::getValue)
                    .ifPresent(aciov -> message.add(aciov.getRaw()));
        }

        return new Parameters(message.toString(), paramObjects);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(gateway == null) {
            return Mono.error(
                    new BotConfigurationException("Attempted to stop bot on Discord, but it was never running"));
        }
        LOGGER.debug("Stopping bot on Discord");
        return gateway.logout()
                .thenReturn(true);
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.defer(() -> {
            if(gateway == null) {
                return Mono.error(
                        new BotConfigurationException("Attempted to retrieve person on Discord, but it was never running"));
            } else if(id.contains(MULTI_ID_DELIMITER)) {
                return Flux.fromArray(id.split(MULTI_ID_DELIMITER))
                        .flatMap(this::getPersonById)
                        .collectList()
                        .map(MultiPerson::new);
            } else {
                return getPersonById(id);
            }
        })
        .cast(Person.class)
        .defaultIfEmpty(NonePerson.INSTANCE)
        .onErrorReturn(NonePerson.INSTANCE);
    }

    private Mono<Person> getPersonById(String id) {
        if(!id.contains(URN_DELIMITER) && StringUtils.isNumeric(id)) {
            return getPersonByUserId(id);
        }
        String[] typeAndOthers = id.split(URN_DELIMITER);
        switch (typeAndOthers[0]) {
            case "member":
                return getPersonByGuildAndUserId(typeAndOthers[1], typeAndOthers[2]);
            case "role":
                if(typeAndOthers[2].equals(EVERYTHING_ID)) {
                    return getPersonByGuild(typeAndOthers[1]);
                } else {
                    return getPersonByGuildAndRoleId(typeAndOthers[1], typeAndOthers[2]);
                }
            case "user":
                return getPersonByUserId(typeAndOthers[1]);
            case "owner":
                return getPersonByOwnerStatus();
            case "self":
                return getPersonBySelf();
            default:
                return Mono.error(
                        new IllegalArgumentException("Unknown person ID format " + typeAndOthers[0]));
        }
    }

    private Mono<Person> getPersonByGuildAndUserId(String guildId, String userId) {
        return gateway.getMemberById(Snowflake.of(guildId), Snowflake.of(userId))
                .map(DiscordPerson::new);
    }

    private Mono<Person> getPersonByGuild(String guildId) {
        return gateway.getGuildById(Snowflake.of(guildId))
                .flatMap(Guild::getEveryoneRole)
                .map(DiscordRolePerson::new);
    }

    private Mono<Person> getPersonByGuildAndRoleId(String guildId, String roleId) {
        return gateway.getRoleById(Snowflake.of(guildId), Snowflake.of(roleId))
                .map(DiscordRolePerson::new);
    }

    private Mono<Person> getPersonByUserId(String userId) {
        return gateway.getUserById(Snowflake.of(userId))
                .map(DiscordPerson::new);
    }

    private Mono<Person> getPersonByOwnerStatus() {
        return gateway.getApplicationInfo()
                .flatMap(ApplicationInfo::getOwner)
                .map(DiscordPerson::new);
    }

    private Mono<Person> getPersonBySelf() {
        return gateway.getSelf()
                .map(DiscordPerson::new);
    }

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.defer(() -> {
            if(gateway == null) {
                return Mono.error(
                        new BotConfigurationException("Attempted to retrieve place on Discord, but platform was not running"));
            }
            if (id.contains(MULTI_ID_DELIMITER)) {
                String[] ids = id.split(MULTI_ID_DELIMITER);
                return Flux.fromArray(ids)
                        .flatMap(this::getPlaceById)
                        .collectList()
                        .map(MultiPlace::new);
            } else {
                return getPlaceById(id);
            }
        })
        .cast(Place.class)
        .defaultIfEmpty(NonePlace.INSTANCE)
        .onErrorReturn(NonePlace.INSTANCE);
    }

    private Mono<Place> getPlaceById(String id) {
        if(!id.contains(URN_DELIMITER)) {
            return getPlaceByChannelId(id);
        }
        String[] typeAndOthers = id.split(URN_DELIMITER);
        switch (typeAndOthers[0]) {
            case "guild":
                if(typeAndOthers[1].equals(EVERYTHING_ID)) {
                    return getPlaceByEverywhere();
                } else {
                    return getPlaceByGuildId(typeAndOthers[1]);
                }
            case "channel":
                return getPlaceByChannelId(typeAndOthers[1]);
            default:
                return Mono.error(
                        new IllegalArgumentException("Unknown place ID format " + typeAndOthers[0]));
        }
    }

    private Mono<Place> getPlaceByEverywhere() {
        return Mono.just(new DiscordEverywhere(gateway));
    }

    private Mono<Place> getPlaceByGuildId(String guildId) {
        return gateway.getGuildById(Snowflake.of(guildId))
                .map(DiscordGuild::new);
    }

    private Mono<Place> getPlaceByChannelId(String channelId) {
        Snowflake place = Snowflake.of(channelId);
        return gateway.getChannelById(place)
                .cast(TextChannel.class)
                .map(DiscordPlace::new);
    }

    public void registerHook(String id, ReactiveEventAdapter hook) {
        LOGGER.debug("Applying custom hook {}",id);
        subscriptionMap.put(id, gateway.on(hook)
                .doOnError(CancelHookException.class, ex -> deregisterHook(id))
                .subscribe());
    }

    public void registerHook(ReactiveEventAdapter hook) {
        registerHook(UUID.randomUUID().toString(), hook);
    }

    public void deregisterHook(String id) {
        LOGGER.debug("Removing custom hook {}",id);
        subscriptionMap.remove(id);
    }

    @Override
    public Health health() {
        if(gateway == null) {
            return Health.outOfService().withDetail("reason", "Not started").build();
        } else {
            try {
                User user = gateway.getSelf().block(Duration.ofSeconds(10));
                if(user == null) {
                    return Health.unknown().withDetail("reason", "No Self?").build();
                }
                return Health.up()
                        .withDetail("id", user.getId().asLong())
                        .withDetail("name", user.getUsername())
                        .withDetail("discriminator", user.getDiscriminator())
                        .withDetail("tag", user.getTag())
                        .build();
            } catch (RuntimeException ex) {
                return Health.down()
                        .withDetail("reason", ex.getCause().getClass().getCanonicalName())
                        .withDetail("message", ex.getCause().getMessage())
                        .build();
            }
        }
    }

    @Override
    public void contribute(Info.Builder builder) {
        if(gateway != null) {
            try {
                User user = gateway.getSelf().block(Duration.ofSeconds(10));
                if(user != null) {
                    Map<String, Object> discordObj = new HashMap<>(4);
                    discordObj.put("id", user.getId().asLong());
                    discordObj.put("name", user.getUsername());
                    discordObj.put("discriminator", user.getDiscriminator());
                    discordObj.put("tag", user.getTag());

                    builder.withDetails(Collections.singletonMap("discord", discordObj));
                }
            } catch (RuntimeException ex) { }
        }
    }
}

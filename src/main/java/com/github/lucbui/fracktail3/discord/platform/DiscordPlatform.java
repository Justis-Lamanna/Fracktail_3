package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.*;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.*;
import com.github.lucbui.fracktail3.magic.platform.context.BasicCommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.TextChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.regex.Pattern;

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
public class DiscordPlatform implements Platform {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatform.class);
    private static final String EVERYTHING_ID = "*";
    private static final String MULTI_ID_DELIMITER = ";";
    private static final String URN_DELIMITER = ":";

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\s+");

    private final DiscordConfiguration configuration;
    private GatewayDiscordClient gateway;

    /**
     * Initialize this platform with a configuration
     * @param configuration The configuration to use
     */
    public DiscordPlatform(DiscordConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getId() {
        return "discord";
    }

    public DiscordConfiguration getConfig() {
        return configuration;
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

        gateway.updatePresence(configuration.getPresence()).block();

        getPlaceByEverywhere()
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
                                String[] pArray = SPLIT_PATTERN.split(pStr);
                                return new BasicCommandUseContext(bot, this, message, t.getT2(), new Parameters(pStr, pArray));
                            })
                            .filterWhen(CommandUseContext::matches)
                            .next()
                            .flatMap(CommandUseContext::doAction)
                            .onErrorResume(Throwable.class, e -> {
                                e.printStackTrace();
                                return message.getOrigin()
                                        .flatMap(place ->
                                                place.sendMessage("I ran into an error there, sorry: " + e.getMessage() + ". Check the logs for more info."))
                                        .then();
                            });
                })
                .subscribe();

        return gateway.onDisconnect().thenReturn(true);
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
        if(!id.contains(URN_DELIMITER)) {
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

    public static class Builder implements IBuilder<DiscordPlatform> {
        private DiscordConfiguration configuration;

        public Builder withConfiguration(DiscordConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withConfiguration(IBuilder<DiscordConfiguration> configuration) {
            this.configuration = configuration.build();
            return this;
        }

        @Override
        public DiscordPlatform build() {
            return new DiscordPlatform(configuration);
        }
    }
}

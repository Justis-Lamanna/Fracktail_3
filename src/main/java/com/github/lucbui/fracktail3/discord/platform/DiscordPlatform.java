package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.*;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.*;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * A singleton which represents the Discord platform
 */
public class DiscordPlatform implements Platform {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatform.class);

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

    @Override
    public DiscordConfiguration getConfig() {
        return configuration;
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(gateway != null) {
            return Mono.error(new BotConfigurationException("Attempted to start bot on Discord, but it was already running"));
        }

        BotSpec botSpec = bot.getSpec();

        DiscordCommandHandler discordCommandHandler = new DefaultDiscordCommandHandler(botSpec.getCommandList());

        DiscordClient discordClient =
                DiscordClientBuilder.create(configuration.getToken()).build();

        LOGGER.debug("Starting bot on Discord");
        gateway = discordClient.login().block();

        if(gateway == null) {
            throw new BotConfigurationException("Gateway was null");
        }

        gateway.updatePresence(configuration.getPresence()).block();

//        gateway.on(MessageCreateEvent.class)
//                .flatMap(msg -> discordCommandHandler.execute(bot, this, msg))
//                .subscribe();
        getPlace("*")
                .flatMapMany(Place::getMessageFeed)
                .filter(msg -> !msg.getSender().equals(NonePerson.INSTANCE))
                .filter(msg -> !msg.getSender().isBot())
                .doOnNext(msg -> System.out.println(msg.getSender().getName() + ": " + msg.getContent()))
                //.flatMap(msg -> discordCommandHandler.execute(bot, this, msg))
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
            } else if(id.contains(":")) {
                String[] typeAndOthers = id.split(":");
                switch (typeAndOthers[0]) {
                    case "member":
                        return gateway.getMemberById(Snowflake.of(typeAndOthers[1]), Snowflake.of(typeAndOthers[2]))
                                .map(DiscordMemberPerson::new);
                    case "role":
                        return gateway.getRoleById(Snowflake.of(typeAndOthers[1]), Snowflake.of(typeAndOthers[2]))
                                .map(DiscordRolePerson::new);
                    case "user":
                        return gateway.getUserById(Snowflake.of(typeAndOthers[1]))
                                .map(DiscordPerson::new);
                    default:
                        return Mono.error(
                                new IllegalArgumentException("Unknown person ID format " + typeAndOthers[0]));
                }
            } else {
                return gateway.getUserById(Snowflake.of(id))
                        .map(DiscordPerson::new);
            }
        })
        .cast(Person.class)
        .defaultIfEmpty(NonePerson.INSTANCE)
        .onErrorReturn(NonePerson.INSTANCE);
    }

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.defer(() -> {
            if(gateway == null) {
                return Mono.error(
                        new BotConfigurationException("Attempted to retrieve place on Discord, but platform was not running"));
            }
            if(id.equals("*")) {
                return Mono.just(new DiscordEverywhere(gateway));
            } else {
                Snowflake place = Snowflake.of(id);
                return gateway.getChannelById(place)
                        .cast(TextChannel.class)
                        .map(DiscordPlace::new)
                        .cast(Place.class)
                        .defaultIfEmpty(NonePlace.INSTANCE)
                        .onErrorReturn(NonePlace.INSTANCE);
            }
        });
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

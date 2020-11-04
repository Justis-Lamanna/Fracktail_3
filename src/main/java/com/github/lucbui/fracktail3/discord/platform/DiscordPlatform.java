package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.guard.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DefaultDiscordOnEventHandler;
import com.github.lucbui.fracktail3.discord.hook.DiscordOnEventHandler;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
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
        DiscordOnEventHandler discordEventHandler = new DefaultDiscordOnEventHandler();

        DiscordClient discordClient =
                DiscordClientBuilder.create(configuration.getToken()).build();

        LOGGER.debug("Starting bot on Discord");
        gateway = discordClient.login().block();

        if(gateway == null) {
            throw new BotConfigurationException("Gateway was null");
        }

        gateway.updatePresence(configuration.getPresence()).block();

        gateway.on(MessageCreateEvent.class)
                .flatMap(msg -> discordCommandHandler.execute(bot, this, msg))
                .subscribe();

        gateway.on(Event.class)
                .flatMap(evt -> discordEventHandler.execute(bot, this, evt))
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

    /**
     * Send a message to all members of a DiscordChannelset.
     * Note, if you send DiscordChannelset.ALL_CHANNELS, you *will* send a message in every single channel the bot knows.
     * @param channelset The channelset to send to
     * @param message The message to say
     * @return Asynchronous indication of completion
     */
    public Mono<Void> message(DiscordChannelset channelset, String message) {
        if(channelset.matchesEveryChannel()) {
            //Ho boy...
            return gateway.getGuilds()
                    .flatMap(Guild::getChannels)
                    .cast(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage(message))
                    .next()
                    .then();
        }
        if(channelset.matchesNoChannel()) {
            // Short-circuit. Don't even bother iterating.
            return Mono.empty();
        }
        return Flux.fromIterable(channelset.getChannelSnowflakes())
                .flatMap(channel -> gateway.getChannelById(channel))
                .cast(MessageChannel.class)
                .flatMap(channel -> channel.createMessage(message))
                .next()
                .then();
    }

    /**
     * Send a dm to all members of a DiscordUserset.
     * Note, if you send DiscordUserset.ALL_USERS, you *will* send a message in every single user the bot knows.
     * @param userset The users to message
     * @param message The message to say
     * @return Asynchronous indication of completion
     */
    public Mono<Void> dm(DiscordUserset userset, String message) {
        if(userset.matchesEveryUser()) {
            return gateway.getUsers()
                    .flatMap(User::getPrivateChannel)
                    .flatMap(dm -> dm.createMessage(message))
                    .next()
                    .then();
        }
        if(userset.matchesNoUser()) {
            // Short-circuit. Don't even bother iterating.
            return Mono.empty();
        }
        return Flux.fromIterable(userset.getUserSnowflakes())
                .flatMap(user -> gateway.getUserById(user))
                .flatMap(User::getPrivateChannel)
                .flatMap(dm -> dm.createMessage(message))
                .next()
                .then();
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

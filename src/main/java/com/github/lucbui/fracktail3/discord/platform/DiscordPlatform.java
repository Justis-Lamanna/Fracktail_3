package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.DiscordBaseContext;
import com.github.lucbui.fracktail3.discord.hook.DefaultDiscordOnEventHandler;
import com.github.lucbui.fracktail3.discord.hook.DiscordOnEventHandler;
import com.github.lucbui.fracktail3.discord.schedule.DiscordScheduleContext;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.ScheduleSubscriber;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Instant;

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
        DiscordOnEventHandler discordEventHandler = new DefaultDiscordOnEventHandler(configuration.getHandlers());

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
                .flatMap(evt -> discordEventHandler.execute(bot, configuration, evt))
                .subscribe();

        configureScheduledEvents(bot);

        return gateway.onDisconnect().thenReturn(true);
    }

    private void configureScheduledEvents(Bot bot) {
        for(ScheduledEvent event : configuration.getScheduledEvents().getAll()) {
            event.getTrigger()
                    .schedule(bot.getScheduler())
                    .subscribe(new ScheduleSubscriber(event, instant -> {
                        DiscordBaseContext<Instant> base = new DiscordBaseContext<>(bot, this, instant);
                        return new DiscordScheduleContext(base, event, gateway);
                    }));
        }
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(gateway == null) {
            return Mono.error(
                    new BotConfigurationException("Attempted to stop bot on Discord, but it was never running"));
        }
        LOGGER.debug("Stopping bot on Discord");
        cancelEvents();
        return gateway.logout()
                .thenReturn(true);
    }

    private void cancelEvents() {
        configuration
                .getScheduledEvents()
                .getAll()
                .forEach(ScheduledEvent::cancel);
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

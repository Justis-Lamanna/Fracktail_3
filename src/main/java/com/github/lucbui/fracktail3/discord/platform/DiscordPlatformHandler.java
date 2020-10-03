package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.event.DefaultHookEventFactory;
import com.github.lucbui.fracktail3.discord.event.HookEventFactory;
import com.github.lucbui.fracktail3.discord.hook.DefaultDiscordOnEventHandler;
import com.github.lucbui.fracktail3.discord.hook.DiscordOnEventHandler;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Encapsulates how the Discord platform starts and stops the bot.
 */
public class DiscordPlatformHandler implements PlatformHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatformHandler.class);

    private final DiscordPlatform platform;
    private final HookEventFactory hookEventFactory;
    private GatewayDiscordClient gateway;

    public DiscordPlatformHandler(DiscordPlatform platform) {
        this.platform = platform;
        this.hookEventFactory = new DefaultHookEventFactory();
    }

    @Override
    public String getId() {
        return platform.getId();
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(gateway != null) {
            throw new BotConfigurationException("Bot is already started.");
        }

        BotSpec botSpec = bot.getSpec();
        DiscordConfiguration configuration = platform.getConfig();

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
                .flatMap(msg -> discordCommandHandler.execute(bot, configuration, msg))
                .subscribe();

        gateway.on(Event.class)
                .map(hookEventFactory::fromEvent)
                .flatMap(evt -> discordEventHandler.execute(bot, configuration, evt))
                .subscribe();

        return gateway.onDisconnect().thenReturn(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(gateway == null) {
            LOGGER.debug("Attempted to stop bot with Discord Runner, without a Discord configuration");
            return Mono.empty();
        }
        LOGGER.debug("Stopping bot on Discord");
        return gateway.logout().thenReturn(true);
    }
}

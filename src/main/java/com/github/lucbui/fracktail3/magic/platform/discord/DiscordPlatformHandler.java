package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Encapsulates how the Discord platform starts and stops the bot.
 */
public class DiscordPlatformHandler implements PlatformHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatformHandler.class);
    private GatewayDiscordClient gateway;

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(gateway != null) {
            throw new BotConfigurationException("Bot is already started.");
        }

        BotSpec botSpec = bot.getSpec();

        return botSpec.getConfig(DiscordPlatform.INSTANCE).map(discordConfig -> {
            DiscordHandler discordHandler = new CommandListDiscordHandler(botSpec.getBehaviorList().getCommandList());
            DiscordClient discordClient =
                    DiscordClientBuilder.create(discordConfig.getToken()).build();

            LOGGER.debug("Starting bot on Discord");
            gateway = discordClient.login().block();

            if(gateway == null) {
                throw new BotConfigurationException("Gateway was null");
            }

            gateway.updatePresence(discordConfig.getPresence()).block();

            gateway.on(MessageCreateEvent.class)
                .doOnNext(msg -> LOGGER.debug("Received a message: {}", msg.getMessage()))
                .flatMap(msg -> discordHandler.execute(bot, discordConfig, msg))
                .subscribe();

            return gateway.onDisconnect().thenReturn(true);
        }).orElseGet(() -> {
            LOGGER.debug("Attempted to start bot with Discord Runner, without a Discord configuration");
            return Mono.empty();
        });
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

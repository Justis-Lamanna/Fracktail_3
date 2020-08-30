package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.platform.PlatformHandler;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class DiscordPlatformHandler implements PlatformHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordPlatformHandler.class);
    private DiscordClient discordClient;

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(discordClient != null) {
            throw new BotConfigurationException("Bot is already started.");
        }

        BotSpec botSpec = bot.getSpec();
        if(!botSpec.getDiscordConfiguration().isPresent()) {
            LOGGER.debug("Attempted to start bot with Discord Runner, without a Discord configuration");
            return Mono.empty();
        }

        DiscordHandler discordHandler = new CommandListDiscordHandler(botSpec.getCommandList());
        DiscordConfiguration discordConfig = botSpec.getDiscordConfiguration().get();
        discordClient = new DiscordClientBuilder(discordConfig.getToken())
                .setInitialPresence(discordConfig.getPresence())
                .build();

        discordClient.getEventDispatcher().on(MessageCreateEvent.class)
                .doOnNext(msg -> LOGGER.debug("Received a message: {}", msg.getMessage()))
                .flatMap(msg -> discordHandler.execute(bot, discordConfig, msg))
                .subscribe();

        LOGGER.debug("Starting bot on Discord");
        return discordClient.login().thenReturn(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(discordClient == null) {
            LOGGER.debug("Attempted to stop bot with Discord Runner, without a Discord configuration");
            return Mono.empty();
        }
        LOGGER.debug("Stopping bot on Discord");
        return discordClient.logout().thenReturn(true);
    }
}
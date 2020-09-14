package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

/**
 * Sanity functional interface to represent what the do when a message event comes in.
 */
@FunctionalInterface
public interface DiscordHandler {
    /**
     * Execute some action when a message event comes in.
     * @param bot The bot being run
     * @param configuration The DiscordConfiguration
     * @param event The event that came in
     * @return An empty asynchronous value. Result is ignored.
     */
    Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event);
}

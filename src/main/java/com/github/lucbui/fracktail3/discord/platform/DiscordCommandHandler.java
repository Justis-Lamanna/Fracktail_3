package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

/**
 * Sanity functional interface to represent what the do when a message event comes in.
 */
@FunctionalInterface
public interface DiscordCommandHandler {
    /**
     * Execute some action when a message event comes in.
     * @param bot The bot being run
     * @param configuration The DiscordConfiguration
     * @param event The event that came in
     * @return An empty asynchronous value. Result is ignored.
     */
    Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event);
}

package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * Sanity functional interface to represent what to do when any event comes in
 */
public interface DiscordOnEventHandler {
    /**
     * Execute some action when an event comes in.
     * @param bot The bot being run
     * @param configuration The DiscordConfiguration
     * @param event The event that came in
     * @return An empty asynchronous value. Result is ignored.
     */
    Mono<Void> execute(Bot bot, DiscordConfiguration configuration, Event event);
}

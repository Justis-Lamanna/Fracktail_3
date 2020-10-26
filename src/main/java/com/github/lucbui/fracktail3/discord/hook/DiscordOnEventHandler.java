package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * Sanity functional interface to represent what to do when any event comes in
 */
public interface DiscordOnEventHandler {
    /**
     * Execute some action when an event comes in.
     * @param bot The bot being run
     * @param platform The Platform
     * @param event The event that came in
     * @return An empty asynchronous value. Result is ignored.
     */
    Mono<Void> execute(Bot bot, DiscordPlatform platform, Event event);
}

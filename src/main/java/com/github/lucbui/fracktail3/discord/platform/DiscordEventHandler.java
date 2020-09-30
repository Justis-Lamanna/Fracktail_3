package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler<E extends Event> {
    /**
     * Execute the code that can handle this event
     * @param bot The bot being executed on
     * @param ctx The context of the event execution
     * @param event The event being handled
     * @return Asynchronous indication of completion
     */
    Mono<Void> handleEvent(Bot bot, DiscordEventContext ctx, E event);

    /**
     * Create a handler that does nothing
     * @param <E> The type of event accepted
     * @return A no-op handler
     */
    static <E extends Event> DiscordEventHandler<E> noop() {
        return (bot, ctx, evt) -> Mono.empty();
    }
}

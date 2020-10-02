package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.event.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

/**
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler {
    /**
     * Execute the code that can handle this event
     * @param bot The bot being executed on
     * @param ctx The context of the event execution
     * @return Asynchronous indication of completion
     */
    Mono<Void> handleEvent(Bot bot, DiscordEventContext ctx);

    /**
     * Create a handler that does nothing
     * @param <E> The type of event accepted
     * @return A no-op handler
     */
    static <E extends DiscordSupportedEvent> DiscordEventHandler noop() {
        return (bot, ctx) -> Mono.empty();
    }
}

package com.github.lucbui.fracktail3.magic.hook;

import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

public interface EventHookHandler<E extends SupportedEvent> {
    /**
     * Execute the code that can handle this event
     * @param bot The bot being executed on
     * @param ctx The context of the event execution
     * @return Asynchronous indication of completion
     */
    Mono<Void> handleEvent(Bot bot, EventContext<E> ctx);
}

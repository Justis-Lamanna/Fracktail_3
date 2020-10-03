package com.github.lucbui.fracktail3.magic.hook;

import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

public interface EventHookGuard<E extends SupportedEvent> {
    /**
     * Check whether a command can be used in this context.
     * @param bot The bot being ran
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(Bot bot, EventContext<E> ctx);
}

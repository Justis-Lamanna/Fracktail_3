package com.github.lucbui.fracktail3.magic.platform.context;

import reactor.core.publisher.Mono;

/**
 * Marks a context as being able to be responded to
 */
public interface RespondingContext {
    /**
     * Respond to this context, in some way the context sees fit
     * @param message The message to send
     * @return Asynchronous indication of completion
     */
    Mono<Void> respond(String message);
}

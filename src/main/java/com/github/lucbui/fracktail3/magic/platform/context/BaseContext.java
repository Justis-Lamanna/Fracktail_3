package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Base level context with some payload
 * @param <T> The payload
 */
public interface BaseContext<T> {
    Bot getBot();
    T getPayload();

    /**
     * Get an asynchronously-determined Locale
     * @return Locale.getDefault()
     */
    default Mono<Locale> getLocale() {
        return Mono.just(Locale.getDefault());
    }

    /**
     * Get a map of relevant objects, for formatting
     * @return An asynchronously-calculated map
     */
    default AsynchronousMap<String, Object> getMap() {
        return new AsynchronousMap<>();
    }

    /**
     * Respond to this context, in some way the context sees fit
     * @param message The message to send
     * @return Asynchronous indication of completion
     */
    default Mono<Void> respond(String message) {
        return Mono.empty();
    }

    /**
     * Respond to this context, over some form of private communication
     * @param message The message to send
     * @return Asynchronous indication of completion
     */
    default Mono<Void> privateMessage(String message) {
        return Mono.empty();
    }
}

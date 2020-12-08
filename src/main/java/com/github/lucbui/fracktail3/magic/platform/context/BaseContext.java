package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.command.model.BotResponse;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;

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
    Mono<Void> respond(String message);

    /**
     * Respond to this context, in some way the context sees fit
     * @param response The message to send
     * @return Asynchronous indication of completion
     */
    default Mono<Void> respond(BotResponse response) {
        return response.respondWith().getFor(this)
                .flatMap(this::respond);
    }

    /**
     * Respond to this context, in some way the context sees fit
     * @param response The message to send
     * @param addlVariables Additional key/value pairs to use
     * @return Asynchronous indication of completion
     */
    default Mono<Void> respond(BotResponse response, Map<String, Object> addlVariables) {
        return response.respondWith().getFor(this, addlVariables)
                .flatMap(this::respond);
    }
//
//    /**
//     * Respond to this context, over some form of private communication
//     * @param message The message to send
//     * @return Asynchronous indication of completion
//     */
//    default Mono<Void> privateMessage(String message) {
//        return Mono.empty();
//    }
//
//    /**
//     * Respond to this context, over some form of private communication
//     * @param response The message to send
//     * @return Asynchronous indication of completion
//     */
//    default Mono<Void> privateMessage(BotResponse response) {
//        return response.respondWith().getFor(this)
//                .flatMap(this::privateMessage);
//    }
//
//    /**
//     * Respond to this context, over some form of private communication
//     * @param response The message to send
//     * @param addlVariables Additional key/value pairs to use
//     * @return Asynchronous indication of completion
//     */
//    default Mono<Void> privateMessage(BotResponse response, Map<String, Object> addlVariables) {
//        return response.respondWith().getFor(this, addlVariables)
//                .flatMap(this::privateMessage);
//    }
}

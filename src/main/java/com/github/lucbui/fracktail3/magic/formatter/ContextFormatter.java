package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * Describes how a string should be formatted, when combined with a context
 */
public interface ContextFormatter {
    /**
     * Format a string
     * @param raw The string to format
     * @param ctx The context to use in the formatting
     * @return Asynchronously-determined formatted string
     */
    Mono<String> format(String raw, BaseContext<?> ctx);

    /**
     * Formatter which simply returns the input
     * @return An identity formatter which does no formatting
     */
    static ContextFormatter identity() {
        return (raw, ctx) -> Mono.just(raw);
    }
}

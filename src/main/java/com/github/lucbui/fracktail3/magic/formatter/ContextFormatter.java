package com.github.lucbui.fracktail3.magic.formatter;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Describes how a string should be formatted, when combined with a context
 */
public interface ContextFormatter {
    /**
     * Format a string
     * @param raw The string to format
     * @return Asynchronously-determined formatted string
     */
    Mono<String> format(String raw, Map<String, Object> addlVars);

    /**
     * Formatter which simply returns the input
     * @return An identity formatter which does no formatting
     */
    static ContextFormatter identity() {
        return (raw, vars) -> Mono.just(raw);
    }

    /**
     * Combine this ContextFormatter with another.
     * This ContextFormatter performs some transformation, which is fed into the next formatter
     * for additional transformations.
     * @param next The next formatter
     * @return A new formatter which combines this and next.
     */
    default ContextFormatter pipe(ContextFormatter next) {
        ContextFormatter us = this;
        return (raw, vars) -> us.format(raw, vars)
                .flatMap(s -> next.format(s, vars));
    }
}

package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

import java.util.Map;

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
    Mono<String> format(String raw, BaseContext<?> ctx, Map<String, Object> addlVars);

    /**
     * Formatter which simply returns the input
     * @return An identity formatter which does no formatting
     */
    static ContextFormatter identity() {
        return (raw, ctx, vars) -> Mono.just(raw);
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
        return (raw, ctx, vars) -> us.format(raw, ctx, vars).flatMap(s -> next.format(s, ctx, vars));
    }
}

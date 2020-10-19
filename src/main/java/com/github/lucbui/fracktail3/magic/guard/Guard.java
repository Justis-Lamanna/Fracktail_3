package com.github.lucbui.fracktail3.magic.guard;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * An abstract filter, or guard, to lock commands in certain contexts.
 */
public interface Guard {
    /**
     * Check whether a command can be used in this context.
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(BaseContext<?> ctx);

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default Guard and(Guard other) {
        Guard self = this;
        return (ctx) -> BooleanUtils.and(self.matches(ctx), other.matches(ctx));
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default Guard or(Guard other) {
        Guard self = this;
        return (ctx) -> BooleanUtils.or(self.matches(ctx), other.matches(ctx));
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default Guard not() {
        Guard self = this;
        return (ctx) -> BooleanUtils.not(self.matches(ctx));
    }

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static Guard identity(boolean value) {
        return (ctx) -> Mono.just(value);
    }
}

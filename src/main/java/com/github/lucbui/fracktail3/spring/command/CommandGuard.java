package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

public interface CommandGuard {
    /**
     * Check whether a command can be used in this context.
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(CommandUseContext<?> ctx);

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default CommandGuard and(CommandGuard other) {
        CommandGuard self = this;
        return (ctx) -> BooleanUtils.and(self.matches(ctx), other.matches(ctx));
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default CommandGuard or(CommandGuard other) {
        CommandGuard self = this;
        return (ctx) -> BooleanUtils.or(self.matches(ctx), other.matches(ctx));
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default CommandGuard not() {
        CommandGuard self = this;
        return (ctx) -> BooleanUtils.not(self.matches(ctx));
    }

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static CommandGuard identity(boolean value) {
        return (ctx) -> Mono.just(value);
    }
}

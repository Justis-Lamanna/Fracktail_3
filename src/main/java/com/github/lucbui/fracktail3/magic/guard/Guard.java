package com.github.lucbui.fracktail3.magic.guard;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * An abstract filter, or guard, to lock commands in certain contexts.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface Guard {
    /**
     * Check whether a command can be used in this context.
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(CommandUseContext ctx);

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default Guard and(Guard other) {
        return new AndGuard(this, other);
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default Guard or(Guard other) {
        return new OrGuard(this, other);
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default Guard not() {
        return new NotGuard(this);
    }

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static Guard identity(boolean value) {
        return new IdentityGuard(value);
    }
}

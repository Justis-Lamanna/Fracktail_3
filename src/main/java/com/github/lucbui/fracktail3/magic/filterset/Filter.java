package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * An abstract filter, or guard, to lock commands in certain contexts.
 */
public interface Filter {
    /**
     * Check whether a command can be used in this context.
     * @param bot The bot being ran
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(Bot bot, CommandContext ctx);

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default Filter and(Filter other) {
        Filter self = this;
        return (bot, ctx) -> BooleanUtils.and(self.matches(bot, ctx), other.matches(bot, ctx));
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default Filter or(Filter other) {
        Filter self = this;
        return (bot, ctx) -> BooleanUtils.or(self.matches(bot, ctx), other.matches(bot, ctx));
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default Filter not() {
        Filter self = this;
        return (bot, ctx) -> BooleanUtils.not(self.matches(bot, ctx));
    }

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static Filter identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }
}

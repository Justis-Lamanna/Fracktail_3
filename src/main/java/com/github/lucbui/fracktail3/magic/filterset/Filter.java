package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
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
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static Filter identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }
}

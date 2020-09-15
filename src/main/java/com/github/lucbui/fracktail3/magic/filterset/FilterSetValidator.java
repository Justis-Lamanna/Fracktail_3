package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

/**
 * A filter which can be applied to commands to divert their usage.
 */
public interface FilterSetValidator{
    /**
     * Asynchronously validate whether this command can be used for this context
     * @param botSpec The spec being used
     * @param ctx The context the command is being used in
     * @return Asynchronous boolean, indicating if the command can be used or not.
     */
    Mono<Boolean> validate(BotSpec botSpec, CommandContext ctx);

    static FilterSetValidator identity(boolean value) {
        return (spec, ctx) -> Mono.just(value);
    }
}

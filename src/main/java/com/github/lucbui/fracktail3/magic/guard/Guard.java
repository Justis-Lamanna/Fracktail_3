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
}

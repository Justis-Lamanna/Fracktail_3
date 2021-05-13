package com.github.lucbui.fracktail3.magic.command.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * Represents a single action that a bot performs.
 * The doAction method should contain the actual logic this action performs.
 * The guard method, if implemented, should contain any preconditions that must be satisfied to run this command. If
 * a context passes the provided guard, you're basically saying "this action is equipped to handle this particular instance".
 */
@FunctionalInterface
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface CommandAction {
    /**
     * Action which does nothing at all
     */
    CommandAction NOOP = (context) -> Mono.empty();

    /**
     * Perform the action
     * @param context The context of the command usage
     * @return Asynchronous marker indicating action completed
     */
    Mono<Void> doAction(CommandUseContext context);
}

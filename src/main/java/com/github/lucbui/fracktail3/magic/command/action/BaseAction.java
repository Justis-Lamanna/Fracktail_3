package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * An action which can be performed on the most basic of contexts
 */
@FunctionalInterface
public interface BaseAction {
    /**
     * Action which does nothing at all
     */
    BaseAction NOOP = (context) -> Mono.empty();

    /**
     * Perform the action
     * @param context The context of the command usage
     * @return Asynchronous marker indicating action completed
     */
    Mono<Void> doAction(BaseContext context);
}
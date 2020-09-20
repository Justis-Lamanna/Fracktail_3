package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Represents a single action that a bot performs
 */
public interface Action {
    /**
     * Action which does nothing at all
     */
    Action NOOP = (bot, context) -> Mono.empty();

    /**
     * Perform the action
     * @param bot The bot executing
     * @param context The context of the command usage
     * @return Asynchronous marker indicating action completed
     */
    Mono<Void> doAction(Bot bot, CommandContext context);
}

package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.core.publisher.Mono;

/**
 * Represents a single action that a bot performs
 */
@FunctionalInterface
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
    Mono<Void> doAction(CommandUseContext<?> context);

    /**
     * Guard this command from use in a certain context
     * @param context The context to check against
     * @return The guard action
     */
    default Mono<Boolean> guard(PlatformBaseContext<?> context) {
        return Mono.just(true);
    }
}

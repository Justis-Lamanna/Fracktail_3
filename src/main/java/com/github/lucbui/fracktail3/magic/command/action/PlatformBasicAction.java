package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface PlatformBasicAction {
    /**
     * Action which does nothing at all
     */
    CommandAction NOOP = (context) -> Mono.empty();

    /**
     * Perform the action
     * @param context The context of the command usage
     * @return Asynchronous marker indicating action completed
     */
    Mono<Void> doAction(PlatformBaseContext<?> context);
}

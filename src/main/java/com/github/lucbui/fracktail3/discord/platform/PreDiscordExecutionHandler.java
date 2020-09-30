package com.github.lucbui.fracktail3.discord.platform;

import reactor.core.publisher.Mono;

/**
 * Code hook that is run before execution of the action
 */
public interface PreDiscordExecutionHandler {
    /**
     * Code that is run before execution.
     * @param in The DiscordContext being used
     * @return Asynchronous response of a potentially different DiscordContext
     */
    Mono<DiscordContext> beforeExecution(DiscordContext in);

    /**
     * Composes a PreDiscordExecutionHandler which executes this handler, and another one afterwards
     * @param next The next handler to use
     * @return The composed handler which executes this handler, followed by the next one
     */
    default PreDiscordExecutionHandler then(PreDiscordExecutionHandler next) {
        PreDiscordExecutionHandler self = this;
        return in -> self.beforeExecution(in).flatMap(next::beforeExecution);
    }

    /**
     * Identity handler which just returns the input
     * @return An identity handler which does nothing
     */
    static PreDiscordExecutionHandler identity() {
        return Mono::just;
    }
}

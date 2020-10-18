package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.context.DiscordCommandSearchContext;
import com.github.lucbui.fracktail3.discord.context.DiscordCommandUseContext;
import reactor.core.publisher.Mono;

/**
 * Code hook that is run after action execution
 */
public interface DiscordExecutionHook {
    /**
     * Code that is run before execution.
     * @param in The DiscordContext being used
     * @return Asynchronous response of a potentially different DiscordContext
     */
    Mono<DiscordCommandUseContext> beforeExecution(DiscordCommandUseContext in);

    /**
     * Code that is run after successful execution
     * @param in The DiscordContext that was used
     * @return Asynchronous indication of completion
     */
    Mono<Void> afterExecution(DiscordCommandUseContext in);

    /**
     * Code that is run after failed execution
     * @param in The DiscordContext that was used
     * @param exception The exception encountered
     * @return Asynchronous indication of completion
     */
    Mono<Void> afterError(DiscordCommandUseContext in, Exception exception);

    /**
     * Code that is run when no command is found
     * @param in The DiscordContext that was used
     * @return Asynchronous indication of completion
     */
    Mono<Void> onNoCommandFound(DiscordCommandSearchContext in);

    /**
     * Identity handler that does nothing
     * @return Identity handler that does nothing
     */
    static DiscordExecutionHook identity() {
        return Identity.INSTANCE;
    }

    class Identity implements DiscordExecutionHook {
        public static final Identity INSTANCE = new Identity();

        @Override
        public Mono<DiscordCommandUseContext> beforeExecution(DiscordCommandUseContext in) {
            return Mono.just(in);
        }

        @Override
        public Mono<Void> afterExecution(DiscordCommandUseContext in) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> afterError(DiscordCommandUseContext in, Exception exception) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> onNoCommandFound(DiscordCommandSearchContext in) {
            return Mono.empty();
        }
    }
}

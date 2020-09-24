package com.github.lucbui.fracktail3.magic.platform.discord;

import reactor.core.publisher.Mono;

/**
 * Code hook that is run after action execution
 */
public interface PostDiscordExecutionHandler {
    /**
     * Code that is run after successful execution
     * @param in The DiscordContext that was used
     * @return Asynchronous indication of completion
     */
    Mono<Void> afterExecution(DiscordContext in);

    /**
     * Code that is run after failed execution
     * @param in The DiscordContext that was used
     * @param exception The exception encountered
     * @return Asynchronous indication of completion
     */
    Mono<Void> afterError(DiscordContext in, Exception exception);

    /**
     * Identity handler that does nothing
     * @return Identity handler that does nothing
     */
    static PostDiscordExecutionHandler identity() {
        return Identity.INSTANCE;
    }

    /**
     * Composes a PreDiscordExecutionHandler which executes this handler, and another one afterwards
     * @param next The next handler to execute
     * @return A composed handler which executes this handler, followed by the next handler
     */
    default PostDiscordExecutionHandler then(PostDiscordExecutionHandler next) {
        PostDiscordExecutionHandler self = this;
        return new PostDiscordExecutionHandler() {
            @Override
            public Mono<Void> afterExecution(DiscordContext in) {
                return self.afterExecution(in).then(next.afterExecution(in));
            }

            @Override
            public Mono<Void> afterError(DiscordContext in, Exception exception) {
                return self.afterError(in, exception).then(next.afterError(in, exception));
            }
        };
    }

    class Identity implements PostDiscordExecutionHandler {
        public static final Identity INSTANCE = new Identity();

        @Override
        public Mono<Void> afterExecution(DiscordContext in) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> afterError(DiscordContext in, Exception exception) {
            return Mono.empty();
        }
    }
}

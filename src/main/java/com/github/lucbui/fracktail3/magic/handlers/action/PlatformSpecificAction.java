package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import reactor.core.publisher.Mono;

/**
 * An action which only occurs for a specific platform.
 * @param <C> The context type
 */
public abstract class PlatformSpecificAction<C extends CommandContext> implements Action {
    private final Platform<?, C> platform;

    /**
     * Default constructor
     * @param platform The platform to use
     */
    public PlatformSpecificAction(Platform<?, C> platform) {
        this.platform = platform;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return context.castContext(platform)
                .map(c -> doActionForPlatform(bot, c))
                .orElse(doActionForNonPlatform(bot, context));
    }

    /**
     * The action to perform, if the platform is correct
     * @param bot The bot to use.
     * @param context The context to use.
     * @return Asynchronous signal the action was completed.
     */
    protected abstract Mono<Void> doActionForPlatform(Bot bot, C context);

    /**
     * The action to perform, if the platform is incorrect
     * Defaults the an empty Mono.
     * @param bot The bot to use.
     * @param context The context to use.
     * @return Asynchronous signal the action was completed.
     */
    protected Mono<Void> doActionForNonPlatform(Bot bot, CommandContext context) {
        return Mono.empty();
    }
}

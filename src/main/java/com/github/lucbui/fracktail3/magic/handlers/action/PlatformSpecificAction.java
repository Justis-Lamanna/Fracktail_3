package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import reactor.core.publisher.Mono;

/**
 * An action which only occurs for a specific platform.
 * @param <C> The context type
 * @param <P> The platform type
 */
public abstract class PlatformSpecificAction<C extends CommandContext, P extends Platform<?, C, ?>> implements Action {
    private final P platform;

    /**
     * Default constructor
     * @param platform The platform to use
     */
    public PlatformSpecificAction(P platform) {
        this.platform = platform;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return context.castContext(platform)
                .map(c -> doActionForPlatform(bot, c))
                .orElse(Mono.empty());
    }

    /**
     * The action to perform, if the platform is correct
     * @param bot The bot to use.
     * @param context The context to use.
     * @return Asynchronous signal the action was completed.
     */
    protected abstract Mono<Void> doActionForPlatform(Bot bot, C context);
}

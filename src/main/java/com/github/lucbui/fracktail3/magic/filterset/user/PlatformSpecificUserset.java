package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import reactor.core.publisher.Mono;

/**
 * A userset for a specific platform
 * If a request not of this platform is used, false is returned.
 * @param <C> The context type
 * @param <P> The platform type
 */
public abstract class PlatformSpecificUserset<C extends CommandContext, P extends Platform<?, C>> extends Userset {
    private final P platform;

    /**
     * Default constructor
     * @param name Name of the userset
     * @param blacklist If true, negation is used
     * @param extendsRoleset If non-null, extension is used
     * @param platform The platform to work for.
     */
    public PlatformSpecificUserset(String name, boolean blacklist, String extendsRoleset, P platform) {
        super(name, blacklist, extendsRoleset);
        this.platform = platform;
    }

    /**
     * Default constructor, using no negation or extension
     * @param name The name of the userset
     * @param platform The platform of the userset
     */
    public PlatformSpecificUserset(String name, P platform) {
        super(name);
        this.platform = platform;
    }

    @Override
    public Mono<Boolean> matches2(Bot bot, CommandContext context) {
        return context.castContext(platform)
                .map(c -> matchesForPlatform(bot, c))
                .orElse(Mono.just(false));
    }

    /**
     * Called if the platform is correct, using correct casting
     * @param bot The  bot
     * @param context The context of the bot, as casted
     * @return Asynchronous boolean, true if the filter passes.
     */
    protected abstract Mono<Boolean> matchesForPlatform(Bot bot, C context);
}

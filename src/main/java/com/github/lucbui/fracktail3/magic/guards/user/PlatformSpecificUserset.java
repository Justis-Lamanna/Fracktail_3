package com.github.lucbui.fracktail3.magic.guards.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * A userset for a specific platform
 * If a request not of this platform is used, false is returned.
 * @param <C> The context type
 */
public abstract class PlatformSpecificUserset<C extends CommandContext> extends Userset {
    private final Class<C> clazz;

    /**
     * Default constructor, using no negation or extension
     * @param name The name of the userset
     * @param clazz The CommandContext type of the userset
     */
    public PlatformSpecificUserset(String name, Class<C> clazz) {
        super(name);
        this.clazz = clazz;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext context) {
        if(context.getClass().equals(clazz)) {
            return matchesForPlatform(bot, clazz.cast(context));
        }
        return Mono.just(false);
    }

    /**
     * Called if the platform is correct, using correct casting
     * @param bot The  bot
     * @param context The context of the bot, as casted
     * @return Asynchronous boolean, true if the filter passes.
     */
    protected abstract Mono<Boolean> matchesForPlatform(Bot bot, C context);
}

package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

/**
 * A userset for a specific platform
 * If a request not of this platform is used, false is returned.
 * @param <C> The context type
 */
public abstract class PlatformSpecificChannelset<C extends CommandContext> extends Channelset {
    private final Class<C> clazz;

    /**
     * Default constructor, using no negation or extension
     * @param name The name of the channelset
     * @param clazz The type of the channelset
     */
    public PlatformSpecificChannelset(String name, Class<C> clazz) {
        super(name);
        this.clazz = clazz;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext context) {
        if(ClassUtils.isAssignable(context.getClass(), clazz)) {
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

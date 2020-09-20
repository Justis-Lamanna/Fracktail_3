package com.github.lucbui.fracktail3.magic.filterset.channel;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

/**
 * A userset for a specific platform
 * If a request not of this platform is used, false is returned.
 * @param <C> The context type
 */
public abstract class PlatformSpecificChannelset<C extends CommandContext> extends Channelset {
    private final Platform<?> platform;

    /**
     * Default constructor, using no negation or extension
     * @param name The name of the userset
     * @param platform The platform of the userset
     */
    public PlatformSpecificChannelset(String name, Platform<?> platform) {
        super(name);
        this.platform = platform;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext context) {
        if(context.forPlatform(platform)) {
            return matchesForPlatform(bot, (C)context);
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

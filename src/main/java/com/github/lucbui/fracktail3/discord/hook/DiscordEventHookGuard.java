package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.hook.HookEvent;
import reactor.core.publisher.Mono;

/**
 * A guard which can filter out Discord events
 */
public interface DiscordEventHookGuard<HE extends HookEvent<?>> {

    Mono<Boolean> matches(Bot bot, DiscordEventContext<HE> ctx);

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static <HE extends HookEvent<?>> DiscordEventHookGuard<HE> identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }
}

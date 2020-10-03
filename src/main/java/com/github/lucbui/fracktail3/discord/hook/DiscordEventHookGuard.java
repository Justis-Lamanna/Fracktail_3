package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.event.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.hook.EventHookGuard;
import reactor.core.publisher.Mono;

/**
 * A guard which can filter out Discord events
 */
public interface DiscordEventHookGuard extends EventHookGuard<DiscordSupportedEvent> {
    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static DiscordEventHookGuard identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }
}

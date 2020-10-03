package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.event.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.hook.EventHookHandler;
import reactor.core.publisher.Mono;

/**
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler extends EventHookHandler<DiscordSupportedEvent> {
    /**
     * Create a handler that does nothing
     * @param <E> The type of event accepted
     * @return A no-op handler
     */
    static <E extends DiscordSupportedEvent> DiscordEventHandler noop() {
        return (bot, ctx) -> Mono.empty();
    }
}

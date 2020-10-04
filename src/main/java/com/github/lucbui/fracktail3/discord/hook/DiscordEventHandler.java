package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.hook.HookEvent;
import reactor.core.publisher.Mono;

/**
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler<HE extends HookEvent<?>> {
    Mono<Void> handleEvent(Bot bot, DiscordEventContext<HE> ctx);

    /**
     * Create a handler that does nothing
     * @param <HE> The type of event accepted
     * @return A no-op handler
     */
    static <HE extends HookEvent<?>> DiscordEventHandler<HE> noop() {
        return (bot, ctx) -> Mono.empty();
    }
}

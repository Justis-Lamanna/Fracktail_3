package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.hook.DiscordEventContext;
import com.github.lucbui.fracktail3.magic.Bot;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * A guard which can filter out Discord events
 */
public interface DiscordEventHookGuard {
    /**
     * Check whether a command can be used in this context.
     * @param bot The bot being ran
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(Bot bot, DiscordEventContext ctx);

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static DiscordEventHookGuard identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default DiscordEventHookGuard and(DiscordEventHookGuard other) {
        DiscordEventHookGuard self = this;
        return (bot, ctx) -> BooleanUtils.and(self.matches(bot, ctx), other.matches(bot, ctx));
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default DiscordEventHookGuard or(DiscordEventHookGuard other) {
        DiscordEventHookGuard self = this;
        return (bot, ctx) -> BooleanUtils.or(self.matches(bot, ctx), other.matches(bot, ctx));
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default DiscordEventHookGuard not() {
        DiscordEventHookGuard self = this;
        return (bot, ctx) -> BooleanUtils.not(self.matches(bot, ctx));
    }
}

package com.github.lucbui.fracktail3.discord.guards;

import com.github.lucbui.fracktail3.discord.hook.DiscordEventContext;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * A guard which can filter out Discord events
 */
public interface DiscordEventHookGuard<E extends Event> {
    /**
     * Check whether a command can be used in this context.
     * @param bot The bot being ran
     * @param ctx The context the command has been run in
     * @return Asynchronous boolean, indicating if the command can be used or not
     */
    Mono<Boolean> matches(Bot bot, DiscordEventContext ctx, E event);

    /**
     * A simple filter which always returns value
     * @param value The value the filter should return
     * @return The created filter.
     */
    static <E extends Event> DiscordEventHookGuard<E> identity(boolean value) {
        return (bot, ctx, evt) -> Mono.just(value);
    }

    /**
     * Creates a filter that passes when this filter AND another both pass
     * @param other The other filter
     * @return A filter representing this AND other
     */
    default DiscordEventHookGuard<E> and(DiscordEventHookGuard<? super E> other) {
        DiscordEventHookGuard<E> self = this;
        return (bot, ctx, evt) -> BooleanUtils.and(self.matches(bot, ctx, evt), other.matches(bot, ctx, evt));
    }

    /**
     * Creates a filter that passes when this filter OR another pass
     * @param other The other filter
     * @return A filter representing this OR other
     */
    default DiscordEventHookGuard<E> or(DiscordEventHookGuard<? super E> other) {
        DiscordEventHookGuard<E> self = this;
        return (bot, ctx, evt) -> BooleanUtils.or(self.matches(bot, ctx, evt), other.matches(bot, ctx, evt));
    }

    /**
     * Creates a filter that passes when this filter fails
     * @return A filter representing !this
     */
    default DiscordEventHookGuard<E> not() {
        DiscordEventHookGuard<E> self = this;
        return (bot, ctx, evt) -> BooleanUtils.not(self.matches(bot, ctx, evt));
    }
}

package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.guards.DiscordEventHookGuard;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import discord4j.core.event.domain.Event;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

/**
 * A hook that activates when a certain type of event is emitted
 * @param <E> The type of event.
 */
public class DiscordEventHook<E extends Event> implements Id, Disableable {
    private final Class<E> type;
    private final String id;
    private final DiscordEventHookGuard<? super E> guard;
    private final DiscordEventHandler<? super E> handler;

    private boolean enabled;

    /**
     * Create an event hook
     * @param type The type of event to accept.
     * @param id The ID of this hook
     * @param enabled If false, the hook is disabled and does not activate
     * @param guard If resolves as false, the hook is not activated
     * @param handler The code to execute when invoked
     */
    public DiscordEventHook(Class<E> type, String id, boolean enabled, DiscordEventHookGuard<? super E> guard, DiscordEventHandler<? super E> handler) {
        this.type = type;
        this.id = id;
        this.guard = guard;
        this.handler = handler;
        this.enabled = enabled;
    }

    /**
     * Get the type of event accepted
     * @return The type of event accepted
     */
    public Class<E> getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the guard to use
     * @return The guard to use
     */
    public DiscordEventHookGuard<? super E> getGuard() {
        return guard;
    }

    /**
     * Get the handler to invoke
     * @return The handler to invoke
     */
    public DiscordEventHandler<? super E> getHandler() {
        return handler;
    }

    /**
     * Test if the hook is enabled + the guard passes
     * @param bot The bot being run
     * @param ctx The context of the event
     * @return Asynchronous boolean, true if passes
     */
    public Mono<Boolean> passesGuard(Bot bot, DiscordEventContext ctx) {
        boolean enabledAndCorrectType = enabled && ClassUtils.isAssignable(ctx.getEvent().getClass(), type);
        if(!enabledAndCorrectType) {
            return Mono.just(false);
        }
        return getGuard().matches(bot, ctx, type.cast(ctx.getEvent()));
    }

    /**
     * Invoke the handler action
     * @param bot The bot being run
     * @param ctx The context of the event
     * @return Asynchronous indication of completion
     */
    public Mono<Void> doAction(Bot bot, DiscordEventContext ctx) {
        return getHandler().handleEvent(bot, ctx, type.cast(ctx.getEvent()));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Builder which can be used to create an Event Hook
     * @param <E>
     */
    public static class Builder<E extends Event> implements IBuilder<DiscordEventHook<E>> {
        private final Class<E> type;
        private final String id;
        private DiscordEventHookGuard<? super E> guard = DiscordEventHookGuard.identity(true);
        private DiscordEventHandler<? super E> handler = DiscordEventHandler.noop();
        private boolean enabled = true;

        /**
         * Initialize this builder
         * @param id The ID to use
         * @param type The type of event to accept
         */
        public Builder(String id, Class<E> type) {
            this.id = id;
            this.type = type;
        }

        /**
         * Set the guard to use. By default, there is no guard
         * @param guard The guard to use
         * @return This builder
         */
        public Builder<E> setGuard(DiscordEventHookGuard<? super E> guard) {
            this.guard = guard;
            return this;
        }

        /**
         * Set the handler of the hook. By default, nothing happens
         * @param handler The handler to use
         * @return This builder
         */
        public Builder<E> setHandler(DiscordEventHandler<? super E> handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Set if this hook is enabled or disabled
         * @param enabled True, if enabled
         * @return This builder
         */
        public Builder<E> isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public DiscordEventHook<E> build() {
            return new DiscordEventHook<>(type, id, enabled, guard, handler);
        }
    }
}

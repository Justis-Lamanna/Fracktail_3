package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.event.events.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.hook.HookEvent;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * A hook that activates when a certain type of event is emitted
 */
public class DiscordEventHook<E extends Event, HE extends HookEvent<E>> implements Id, Disableable {
    private final String id;
    private final DiscordSupportedEvent<E, HE> supportedEvent;
    private final DiscordEventHookGuard<HE> guard;
    private final DiscordEventHandler<HE> handler;

    private boolean enabled;

    /**
     * Create an event hook
     * @param id The ID of this hook
     * @param supportedEvent Event supported by this hook
     * @param enabled If false, the hook is disabled and does not activate
     * @param guard If resolves as false, the hook is not activated
     * @param handler The code to execute when invoked
     */
    public DiscordEventHook(String id, DiscordSupportedEvent<E, HE> supportedEvent, boolean enabled,
                            DiscordEventHookGuard<HE> guard, DiscordEventHandler<HE> handler) {
        this.id = id;
        this.supportedEvent = supportedEvent;
        this.guard = guard;
        this.handler = handler;
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getId() {
        return id;
    }

    public DiscordSupportedEvent<E, HE> getSupportedEvent() {
        return supportedEvent;
    }

    public DiscordEventHookGuard<HE> getGuard() {
        return guard;
    }

    public DiscordEventHandler<HE> getHandler() {
        return handler;
    }

    public Mono<Void> handleEventIfMatches(Bot bot, DiscordConfiguration config, Event event) {
        if(!enabled) return Mono.empty();
        return Mono.justOrEmpty(supportedEvent.convertIfSupported(event))
                .map(hookEvent -> new DiscordEventContext<>(config, hookEvent))
                .filterWhen(ctx -> getGuard().matches(bot, ctx))
                .flatMap(ctx -> getHandler().handleEvent(bot, ctx));
    }

    /**
     * Builder which can be used to create an Event Hook
     */
    public static class Builder<E extends Event, HE extends HookEvent<E>> implements IBuilder<DiscordEventHook<E, HE>> {
        private final String id;
        private final DiscordSupportedEvent<E, HE> event;
        private DiscordEventHookGuard<HE> guard = DiscordEventHookGuard.identity(true);
        private DiscordEventHandler<HE> handler = DiscordEventHandler.noop();
        private boolean enabled = true;

        /**
         * Initialize this builder
         * @param id The ID to use
         */
        public Builder(String id, DiscordSupportedEvent<E, HE> event) {
            this.id = id;
            this.event = event;
        }

        /**
         * Set the guard to use. By default, there is no guard
         * @param guard The guard to use
         * @return This builder
         */
        public Builder<E, HE> setGuard(DiscordEventHookGuard<HE> guard) {
            this.guard = guard;
            return this;
        }

        /**
         * Set the handler of the hook. By default, nothing happens
         * @param handler The handler to use
         * @return This builder
         */
        public Builder<E, HE> setHandler(DiscordEventHandler<HE> handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Set if this hook is enabled or disabled
         * @param enabled True, if enabled
         * @return This builder
         */
        public Builder<E, HE> isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public DiscordEventHook<E, HE> build() {
            return new DiscordEventHook<>(id, event, enabled, guard, handler);
        }
    }
}

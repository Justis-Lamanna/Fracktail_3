package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.guards.DiscordEventHookGuard;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import reactor.core.publisher.Mono;

/**
 * A hook that activates when a certain type of event is emitted
 */
public class DiscordEventHook implements Id, Disableable {
    private final String id;
    private final DiscordEventHookGuard guard;
    private final DiscordEventHandler handler;

    private boolean enabled;

    /**
     * Create an event hook
     * @param id The ID of this hook
     * @param enabled If false, the hook is disabled and does not activate
     * @param guard If resolves as false, the hook is not activated
     * @param handler The code to execute when invoked
     */
    public DiscordEventHook(String id, boolean enabled, DiscordEventHookGuard guard, DiscordEventHandler handler) {
        this.id = id;
        this.guard = guard;
        this.handler = handler;
        this.enabled = enabled;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the guard to use
     * @return The guard to use
     */
    public DiscordEventHookGuard getGuard() {
        return guard;
    }

    /**
     * Get the handler to invoke
     * @return The handler to invoke
     */
    public DiscordEventHandler getHandler() {
        return handler;
    }

    /**
     * Test if the hook is enabled + the guard passes
     * @param bot The bot being run
     * @param ctx The context of the event
     * @return Asynchronous boolean, true if passes
     */
    public Mono<Boolean> passesGuard(Bot bot, DiscordEventContext ctx) {
        if(!enabled) {
            return Mono.just(false);
        }
        return getGuard().matches(bot, ctx);
    }

    /**
     * Invoke the handler action
     * @param bot The bot being run
     * @param ctx The context of the event
     * @return Asynchronous indication of completion
     */
    public Mono<Void> doAction(Bot bot, DiscordEventContext ctx) {
        return getHandler().handleEvent(bot, ctx);
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
     */
    public static class Builder implements IBuilder<DiscordEventHook> {
        private final String id;
        private DiscordEventHookGuard guard = DiscordEventHookGuard.identity(true);
        private DiscordEventHandler handler = DiscordEventHandler.noop();
        private boolean enabled = true;

        /**
         * Initialize this builder
         * @param id The ID to use
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * Set the guard to use. By default, there is no guard
         * @param guard The guard to use
         * @return This builder
         */
        public Builder setGuard(DiscordEventHookGuard guard) {
            this.guard = guard;
            return this;
        }

        /**
         * Set the handler of the hook. By default, nothing happens
         * @param handler The handler to use
         * @return This builder
         */
        public Builder setHandler(DiscordEventHandler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Set if this hook is enabled or disabled
         * @param enabled True, if enabled
         * @return This builder
         */
        public Builder isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public DiscordEventHook build() {
            return new DiscordEventHook(id, enabled, guard, handler);
        }
    }
}

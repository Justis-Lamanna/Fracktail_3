package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.event.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.hook.EventHook;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * A hook that activates when a certain type of event is emitted
 */
public class DiscordEventHook extends EventHook<DiscordSupportedEvent> implements Id, Disableable {
    /**
     * Create an event hook
     * @param id The ID of this hook
     * @param supportedEvents Events supported by this hook
     * @param enabled If false, the hook is disabled and does not activate
     * @param guard If resolves as false, the hook is not activated
     * @param handler The code to execute when invoked
     */
    public DiscordEventHook(String id, Set<DiscordSupportedEvent> supportedEvents, boolean enabled,
                            DiscordEventHookGuard guard, DiscordEventHandler handler) {
        super(id, supportedEvents, guard, handler, enabled);
    }

    /**
     * Builder which can be used to create an Event Hook
     */
    public static class Builder implements IBuilder<DiscordEventHook> {
        private final String id;
        private final Set<DiscordSupportedEvent> events = EnumSet.noneOf(DiscordSupportedEvent.class);
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
         * Set this hook as supporting the provided event
         * @param event The event to support
         * @return This builder
         */
        public Builder forEvent(DiscordSupportedEvent event) {
            events.add(event);
            return this;
        }

        /**
         * Set this hook as supporting the provided events
         * @param events The events to support
         * @return This builder
         */
        public Builder forEvent(DiscordSupportedEvent... events) {
            this.events.addAll(Arrays.asList(events));
            return this;
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
            return new DiscordEventHook(id, events, enabled, guard, handler);
        }
    }
}

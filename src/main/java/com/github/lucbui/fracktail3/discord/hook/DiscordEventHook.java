package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.util.IBuilder;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * A hook that activates when a certain type of event is emitted
 */
public class DiscordEventHook implements Id, Disableable {
    private final String id;
    private final Set<DiscordSupportedEvents> events;
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
        this(id, EnumSet.allOf(DiscordSupportedEvents.class), enabled, guard, handler);
    }

    /**
     * Create an event hook
     * @param id The ID of this hook
     * @param enabled If false, the hook is disabled and does not activate
     * @param guard If resolves as false, the hook is not activated
     * @param handler The code to execute when invoked
     */
    public DiscordEventHook(String id, Set<DiscordSupportedEvents> events, boolean enabled, DiscordEventHookGuard guard, DiscordEventHandler handler) {
        this.id = id;
        this.events = events;
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

    public Set<DiscordSupportedEvents> getEvents() {
        return events;
    }

    public DiscordEventHookGuard getGuard() {
        return guard;
    }

    public DiscordEventHandler getHandler() {
        return handler;
    }

    /**
     * Builder which can be used to create an Event Hook
     */
    public static class Builder implements IBuilder<DiscordEventHook> {
        private final String id;
        private Set<DiscordSupportedEvents> supported;
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
         * Set the supported events
         * Optional, for now. If null, assumes all events are supported.
         * Allows for optimization on the hook side...maybe?
         * @param event The event type
         * @param more More event types, if necessary
         * @return This builder
         */
        public Builder supports(DiscordSupportedEvents event, DiscordSupportedEvents... more) {
            if(supported == null) {
                supported = EnumSet.of(event, more);
            } else {
                supported.add(event);
                supported.addAll(Arrays.asList(more));
            }
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
            if(supported == null) {
                supported = EnumSet.allOf(DiscordSupportedEvents.class);
            }
            return new DiscordEventHook(id, supported, enabled, guard, handler);
        }
    }
}

package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.Event;

/**
 * A context which contains information about the occured event
 */
public class DiscordEventContext {
    protected final DiscordConfiguration config;
    protected final Event event;

    /**
     * Initialize this context
     * @param config The configuration of this platform
     * @param event The event which occured
     */
    public DiscordEventContext(DiscordConfiguration config, Event event) {
        this.config = config;
        this.event = event;
    }

    /**
     * Get the configuration
     * @return The configuration
     */
    public DiscordConfiguration getConfig() {
        return config;
    }

    /**
     * Get the event
     * @return The event processed
     */
    public Event getEvent() {
        return event;
    }
}

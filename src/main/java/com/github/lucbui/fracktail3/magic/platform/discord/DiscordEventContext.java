package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import discord4j.core.event.domain.Event;

/**
 * A context which contains information about the occured event
 */
public class DiscordEventContext {
    protected final DiscordPlatform platform;
    protected final DiscordConfiguration config;
    protected final Event event;

    /**
     * Initialize this context
     * @param platform The platform of the event
     * @param config The configuration of this platform
     * @param event The event which occured
     */
    public DiscordEventContext(DiscordPlatform platform, DiscordConfiguration config, Event event) {
        this.platform = platform;
        this.config = config;
        this.event = event;
    }

    /**
     * Get the platform
     * @return The command platform
     */
    public DiscordPlatform getPlatform() {
        return platform;
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

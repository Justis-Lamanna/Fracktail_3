package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.hook.HookEvent;

/**
 * A context which contains information about the occured event
 */
public class DiscordEventContext<HE extends HookEvent<?>> {
    protected final DiscordConfiguration config;
    protected final HE event;

    /**
     * Initialize this context
     * @param config The configuration of this platform
     * @param event The event which occured
     */
    public DiscordEventContext(DiscordConfiguration config, HE event) {
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
    public HE getEvent() {
        return event;
    }
}

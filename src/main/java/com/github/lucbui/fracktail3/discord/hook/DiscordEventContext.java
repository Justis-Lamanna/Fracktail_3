package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.event.DiscordHookEvent;
import com.github.lucbui.fracktail3.discord.event.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.magic.hook.EventContext;

/**
 * A context which contains information about the occured event
 */
public class DiscordEventContext extends EventContext<DiscordSupportedEvent> {
    protected final DiscordConfiguration config;
    protected final DiscordHookEvent<?> event;

    /**
     * Initialize this context
     * @param config The configuration of this platform
     * @param event The event which occured
     */
    public DiscordEventContext(DiscordConfiguration config, DiscordHookEvent<?> event) {
        super(config, event);
        this.config = config;
        this.event = event;
    }

    /**
     * Get the configuration
     * @return The configuration
     */
    @Override
    public DiscordConfiguration getConfig() {
        return config;
    }

    /**
     * Get the event
     * @return The event processed
     */
    @Override
    public DiscordHookEvent<?> getEvent() {
        return event;
    }
}

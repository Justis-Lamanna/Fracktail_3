package com.github.lucbui.fracktail3.discord.schedule;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.schedule.ScheduleContext;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import discord4j.core.GatewayDiscordClient;

import java.time.Instant;

/**
 * Context specific to scheduling with Discord
 */
public class DiscordScheduleContext extends ScheduleContext {
    private final DiscordConfiguration config;
    private final GatewayDiscordClient client;

    /**
     * Initialize this context
     * @param config The configuration of the platform
     * @param triggerTime The time the trigger happened
     * @param client The Discord Client, for making calls
     */
    public DiscordScheduleContext(DiscordConfiguration config, ScheduledEvent event, Instant triggerTime, GatewayDiscordClient client) {
        super(config, event, triggerTime);
        this.config = config;
        this.client = client;
    }

    @Override
    public DiscordConfiguration getConfig() {
        return config;
    }

    /**
     * Get a client to make Discord-related calls
     * @return The DiscordClient
     */
    public GatewayDiscordClient getClient() {
        return client;
    }
}

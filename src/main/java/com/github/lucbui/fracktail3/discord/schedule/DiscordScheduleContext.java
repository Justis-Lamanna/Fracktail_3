package com.github.lucbui.fracktail3.discord.schedule;

import com.github.lucbui.fracktail3.discord.context.DiscordBaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import discord4j.core.GatewayDiscordClient;

import java.time.Instant;

/**
 * Context specific to scheduling with Discord
 */
public class DiscordScheduleContext extends DiscordBaseContext<Instant> implements ScheduledUseContext {
    private final ScheduledEvent scheduledEvent;
    private final GatewayDiscordClient client;

    public DiscordScheduleContext(DiscordBaseContext<Instant> base, ScheduledEvent event, GatewayDiscordClient client) {
        super(base);
        this.scheduledEvent = event;
        this.client = client;
    }

    /**
     * Get a client to make Discord-related calls
     * @return The DiscordClient
     */
    public GatewayDiscordClient getClient() {
        return client;
    }

    @Override
    public ScheduledEvent getScheduledEvent() {
        return scheduledEvent;
    }
}

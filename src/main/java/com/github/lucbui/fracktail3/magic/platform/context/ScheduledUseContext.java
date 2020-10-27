package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;

import java.time.Instant;

public class ScheduledUseContext implements BaseContext<Instant> {
    private final Bot bot;
    private final Instant payload;
    private final ScheduledEvent scheduledEvent;

    public ScheduledUseContext(Bot bot, Instant payload, ScheduledEvent event) {
        this.bot = bot;
        this.payload = payload;
        scheduledEvent = event;
    }

    @Override
    public Bot getBot() {
        return bot;
    }

    @Override
    public Instant getPayload() {
        return payload;
    }

    public ScheduledEvent getScheduledEvent() {
        return scheduledEvent;
    }
}

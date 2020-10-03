package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;

/**
 * Convert other events into Hook Events
 */
public interface HookEventFactory {
    /**
     * Create a hook event from a Discord event
     * @param event The event to convert
     * @return The created event
     */
    HookEvent fromEvent(Event event);
}

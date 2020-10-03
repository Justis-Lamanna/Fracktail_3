package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;

/**
 * Default HookEvent which simply wraps the corresponding DiscordEvent
 */
public class WrappedHookEvent implements HookEvent {
    private final DiscordSupportedEvent type;
    private final Event event;

    /**
     * Initialize this hook event
     * @param type The type of event
     * @param event The event being wrapped
     */
    public WrappedHookEvent(DiscordSupportedEvent type, Event event) {
        this.type = type;
        this.event = event;
    }

    @Override
    public DiscordSupportedEvent eventType() {
        return type;
    }

    @Override
    public Event getOriginalEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "WrappedHookEvent{" +
                "type=" + type +
                ", event=" + event +
                '}';
    }
}

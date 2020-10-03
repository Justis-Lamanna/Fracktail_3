package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;

/**
 * Default HookEvent which simply wraps the corresponding DiscordEvent
 */
public class WrappedDiscordHookEvent implements DiscordHookEvent<Event> {
    private final DiscordSupportedEvent type;
    private final Event event;

    /**
     * Initialize this hook event
     * @param type The type of event
     * @param event The event being wrapped
     */
    public WrappedDiscordHookEvent(DiscordSupportedEvent type, Event event) {
        this.type = type;
        this.event = event;
    }

    @Override
    public String toString() {
        return "WrappedHookEvent{" +
                "type=" + type +
                ", event=" + event +
                '}';
    }

    @Override
    public DiscordSupportedEvent getEventType() {
        return type;
    }

    @Override
    public Event getRawEvent() {
        return event;
    }
}

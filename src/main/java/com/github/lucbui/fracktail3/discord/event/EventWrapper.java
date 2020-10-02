package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;

/**
 * A generic wrapper which wraps any DiscordEvent
 * Allows usage of raw events, rather than the cleaner ones
 * @param <E> The event to wrap
 */
public class EventWrapper<E extends Event> implements DiscordSupportedEvent {
    private final E event;

    public EventWrapper(E event) {
        this.event = event;
    }

    public E getRawEvent() {
        return event;
    }
}

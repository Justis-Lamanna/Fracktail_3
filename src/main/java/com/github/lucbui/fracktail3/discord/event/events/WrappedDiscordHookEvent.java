package com.github.lucbui.fracktail3.discord.event.events;

import com.github.lucbui.fracktail3.magic.hook.HookEvent;
import discord4j.core.event.domain.Event;

/**
 * A basic HookEvent which just wraps the Discord event
 * @param <E> The raw Discord event
 */
public class WrappedDiscordHookEvent<E extends Event> implements HookEvent<E> {
    private final E event;

    /**
     * Wrap a raw Discord event into a HookEvent
     * @param event The event to wrap
     */
    public WrappedDiscordHookEvent(E event) {
        this.event = event;
    }

    @Override
    public E getRawEvent() {
        return event;
    }
}

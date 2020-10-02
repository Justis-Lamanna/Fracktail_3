package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;

public class DiscordSupportedEventFactory {
    public DiscordSupportedEvent forEvent(Event event) {
        return new EventWrapper<>(event);
    }
}

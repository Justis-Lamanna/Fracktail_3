package com.github.lucbui.fracktail3.magic.handlers.discord;

import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Locale;

public class DiscordContext extends CommandContext {
    MessageCreateEvent message;
    Locale locale;

    public MessageCreateEvent getMessage() {
        return message;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isDm() {
        return !message.getMember().isPresent();
    }
}

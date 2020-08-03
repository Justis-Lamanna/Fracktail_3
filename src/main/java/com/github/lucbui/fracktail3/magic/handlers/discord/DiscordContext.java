package com.github.lucbui.fracktail3.magic.handlers.discord;

import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Locale;

public class DiscordContext {
    MessageCreateEvent message;
    Locale locale;
    String contents;
    String command;
    String normalizedCommand;
    String parameters;
    String[] normalizedParameters;

    public MessageCreateEvent getMessage() {
        return message;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getContents() {
        return contents;
    }

    public String getCommand() {
        return command;
    }

    public String getNormalizedCommand() {
        return normalizedCommand;
    }

    public String getParameters() {
        return parameters;
    }

    public String[] getNormalizedParameters() {
        return normalizedParameters;
    }
}

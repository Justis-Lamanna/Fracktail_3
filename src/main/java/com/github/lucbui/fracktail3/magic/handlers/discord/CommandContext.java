package com.github.lucbui.fracktail3.magic.handlers.discord;

public class CommandContext {
    String contents;
    String command;
    String normalizedCommand;
    String parameters;
    String[] normalizedParameters;

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

    public boolean isDiscord() {
        return this instanceof DiscordContext;
    }

    public boolean isUnknown() {
        return !isDiscord();
    }
}

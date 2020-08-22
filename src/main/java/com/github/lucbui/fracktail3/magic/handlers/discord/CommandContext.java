package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.handlers.NamedParameters;

public class CommandContext {
    private NamedParameters namedParameters;
    private String contents;
    private String command;
    private String normalizedCommand;
    private String parameters;
    private String[] normalizedParameters;

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

    public NamedParameters getNamedParameters() {
        return namedParameters;
    }

    public void setNamedParameters(NamedParameters namedParameters) {
        this.namedParameters = namedParameters;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setNormalizedCommand(String normalizedCommand) {
        this.normalizedCommand = normalizedCommand;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setNormalizedParameters(String[] normalizedParameters) {
        this.normalizedParameters = normalizedParameters;
    }
}

package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;

import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    public static final String MESSAGE = "message";
    public static final String USED_COMMAND = "usedCommand";
    public static final String COMMAND = "command";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";

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

    public Map<String, String> getVariableMap() {
        Map<String, String> map = new HashMap<>();
        map.put(MESSAGE, contents);
        map.put(USED_COMMAND, command);
        map.put(COMMAND, normalizedCommand);
        map.put(PARAMS, parameters);
        for(int idx = 0; idx < normalizedParameters.length; idx++) {
            map.put(PARAM_PREFIX + idx, normalizedParameters[idx]);
        }
        return map;
    }
}

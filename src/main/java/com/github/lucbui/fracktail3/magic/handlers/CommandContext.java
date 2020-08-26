package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandContext {
    public static final String MESSAGE = "message";
    public static final String USED_COMMAND = "usedCommand";
    public static final String COMMAND = "command";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";
    public static final String RESULT_PREFIX = "result.";

    private String contents;
    private String command;
    private String normalizedCommand;
    private String parameters;
    private String[] normalizedParameters;
    private Map<String, Object> results = new HashMap<>();

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

    public Map<String, Object> getResults() {
        return Collections.unmodifiableMap(results);
    }

    public Object getResult(String key) {
        return results.get(key);
    }

    public <T> T getResult(String key, Class<T> clazz) {
        return clazz.cast(results.get(key));
    }

    public void setResult(String key, Object value) {
        results.put(key, value);
    }

    public Map<String, Object> getVariableMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, contents);
        map.put(USED_COMMAND, command);
        map.put(COMMAND, normalizedCommand);
        map.put(PARAMS, parameters);
        for(int idx = 0; idx < normalizedParameters.length; idx++) {
            map.put(PARAM_PREFIX + idx, normalizedParameters[idx]);
        }
        for(String key : results.keySet()) {
            map.put(RESULT_PREFIX + key, results.get(key));
        }
        return map;
    }
}

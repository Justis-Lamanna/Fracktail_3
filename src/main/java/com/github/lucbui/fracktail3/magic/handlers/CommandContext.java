package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandContext<THIS extends CommandContext<?>> {
    public static final String MESSAGE = "message";
    public static final String USED_COMMAND = "usedCommand";
    public static final String COMMAND = "command";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";
    public static final String RESULT_PREFIX = "result.";

    private String contents;
    private Command.Resolved resolvedCommand;
    private String parameters;
    private String[] normalizedParameters;
    private final Map<String, Object> results = new HashMap<>();

    public String getContents() {
        return contents;
    }

    public Command.Resolved getResolvedCommand() {
        return resolvedCommand;
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

    public THIS setContents(String contents) {
        this.contents = contents;
        return (THIS) this;
    }

    public THIS setResolvedCommand(Command.Resolved resolvedCommand) {
        this.resolvedCommand = resolvedCommand;
        return (THIS) this;
    }

    public THIS setParameters(String parameters) {
        this.parameters = parameters;
        return (THIS) this;
    }

    public THIS setNormalizedParameters(String[] normalizedParameters) {
        this.normalizedParameters = normalizedParameters;
        return (THIS) this;
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
        map.put(USED_COMMAND, resolvedCommand.getId());
        //map.put(COMMAND, normalizedCommand);
        map.put(PARAMS, parameters);
        for(int idx = 0; idx < normalizedParameters.length; idx++) {
            map.put(PARAM_PREFIX + idx, normalizedParameters[idx]);
        }
        for(String key : results.keySet()) {
            map.put(RESULT_PREFIX + key, results.get(key));
        }
        return map;
    }

    public abstract Mono<Boolean> respond(String message);
}

package com.github.lucbui.fracktail3.magic.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class CommandContext {
    public static final String MESSAGE = "message";
    public static final String USED_COMMAND = "usedCommand";
    public static final String COMMAND = "command";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";
    public static final String RESULT_PREFIX = "result.";

    private String contents;
    private String parameters;
    private String[] normalizedParameters;
    private final Map<String, Object> results = new HashMap<>();
    private Locale locale;
    private Command command;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String[] getNormalizedParameters() {
        return normalizedParameters;
    }

    public void setNormalizedParameters(String[] normalizedParameters) {
        this.normalizedParameters = normalizedParameters;
    }

    public Map<String, Object> getResults() {
        return Collections.unmodifiableMap(results);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @JsonIgnore
    public int getParameterCount() {
        return normalizedParameters.length;
    }

    public boolean isDiscord() {
        return this instanceof DiscordContext;
    }

    public boolean isUnknown() {
        return !isDiscord();
    }

    public abstract Config getConfiguration();

    public Object getResult(String key) {
        return results.get(key);
    }

    public <T> T getResult(String key, Class<T> clazz) {
        return clazz.cast(results.get(key));
    }

    public void setResult(String key, Object value) {
        results.put(key, value);
    }

    protected Map<String, Object> getVariableMapConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, contents);
        map.put(PARAMS, parameters);
        for(int idx = 0; idx < normalizedParameters.length; idx++) {
            map.put(PARAM_PREFIX + idx, normalizedParameters[idx]);
        }
        for(String key : results.keySet()) {
            map.put(RESULT_PREFIX + key, results.get(key));
        }
        return map;
    }

    public Mono<Map<String, Object>> getExtendedVariableMap() {
        return Mono.just(getVariableMapConstants());
    }

    public abstract Mono<Boolean> respond(String message);

    public abstract Mono<Boolean> alert(String message);
}

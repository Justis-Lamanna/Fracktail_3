package com.github.lucbui.fracktail3.magic.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class CommandContext {
    public static final String MESSAGE = "message";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";
    public static final String RESULT_PREFIX = "result.";

    private Platform<?> platform;
    private String contents;
    private Locale locale;

    private String parameters;
    private String[] normalizedParameters;
    private final Map<String, Object> vars = new HashMap<>();

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

    public Map<String, Object> getVars() {
        return Collections.unmodifiableMap(vars);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @JsonIgnore
    public int getParameterCount() {
        return normalizedParameters.length;
    }

    public abstract Config getConfiguration();

    public Object getResult(String key) {
        return vars.get(key);
    }

    public <T> T getResult(String key, Class<T> clazz) {
        return clazz.cast(vars.get(key));
    }

    public void setResult(String key, Object value) {
        vars.put(key, value);
    }

    public Platform<?> getPlatform() {
        return platform;
    }

    public void setPlatform(Platform<?> platform) {
        this.platform = platform;
    }

    public boolean forPlatform(Platform<?> testPlatform) {
        return StringUtils.equals(platform.id(), testPlatform.id());
    }

    protected Map<String, Object> getVariableMapConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, contents);
        map.put(PARAMS, parameters);
        for(int idx = 0; idx < normalizedParameters.length; idx++) {
            map.put(PARAM_PREFIX + idx, normalizedParameters[idx]);
        }
        for(String key : vars.keySet()) {
            map.put(RESULT_PREFIX + key, vars.get(key));
        }
        return map;
    }

    public <T> T resolve(Resolver<T> resolver) {
        return resolver.resolve(getConfiguration(), getLocale());
    }

    public Mono<Map<String, Object>> getExtendedVariableMap() {
        return Mono.just(getVariableMapConstants());
    }

    public abstract Mono<Boolean> respond(String message);

    public abstract Mono<Boolean> alert(String message);
}

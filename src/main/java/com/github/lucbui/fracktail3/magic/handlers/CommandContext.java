package com.github.lucbui.fracktail3.magic.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Context object used when a command is received.
 * Contains relevant fields to be used when a command action is being executed.
 */
public abstract class CommandContext {
    public static final String MESSAGE = "message";
    public static final String PARAMS = "params";
    public static final String PARAM_PREFIX = "param.";
    public static final String RESULT_PREFIX = "result.";

    protected final Platform<?> platform;
    protected final Config config;
    protected final String contents;
    protected final Map<String, Object> vars = new HashMap<>();

    protected Locale locale;
    protected String parameters;
    protected String[] normalizedParameters;

    public CommandContext(Platform<?> platform, Config config, String contents) {
        this.platform = platform;
        this.config = config;
        this.contents = contents;
    }

    /**
     * Get the full contents of the command message
     * @return The full contents of the command message
     */
    public String getContents() {
        return contents;
    }

    /**
     * Get the raw parameters of this command, as a string
     * This is the full contents, minus the command name itself.
     * @return The raw parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Set the raw parameters of this command, as a string
     * This is the full contents, minus the command name itself.
     * @param parameters The raw parameters.
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * Get the normalized parameters of this command.
     * These should be the raw parameters, normalized in some manner, such as space-separated and trimmed.
     * @return The normalized parameters, as a string array.
     */
    public String[] getNormalizedParameters() {
        return normalizedParameters;
    }

    /**
     * Set the normalized parameters of this command.
     * These should be the raw parameters, normalized in some manner, such as space-separated and trimmed.
     * @param normalizedParameters The normalized parameters, as a strnig array.
     */
    public void setNormalizedParameters(String[] normalizedParameters) {
        this.normalizedParameters = normalizedParameters;
    }

    /**
     * Get an unmodifiable copy of the vars in this context.
     * Vars can be used to store extra information, which can be used by the action in some way.
     * @return An unmodifiable copy of the vars in this context.
     */
    public Map<String, Object> getVars() {
        return Collections.unmodifiableMap(vars);
    }

    /**
     * Get the locale of the command user.
     * This is suitable for use in internationalization, or simply using Resource Bundles to change text easier.
     * @return The locale of the user of the command.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale of the command user.
     * This is suitable for use in internationalization, or simply using Resource Bundles to change text easier.
     * @param locale The locale of the user of the command.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Gets the number of parameters used
     * @return The number of parameters used for this command.
     */
    @JsonIgnore
    public int getParameterCount() {
        return ArrayUtils.getLength(this.normalizedParameters);
    }

    /**
     * Get the configuration corresponding to the platform of this command
     * @return The configuration
     */
    public Config getConfiguration() {
        return this.config;
    }

    /**
     * Get a result from the list of vars
     * @param key The var name.
     * @return The value at that object, or null if none associated.
     */
    public Object getResult(String key) {
        return vars.get(key);
    }

    /**
     * Get the result from the list of vars, coerced to a specific class.
     * @param key The var name.
     * @param clazz The class of the result.
     * @param <T> The type of the result.
     * @return The value of the object, coerced to a specific type.
     */
    public <T> T getResult(String key, Class<T> clazz) {
        return clazz.cast(vars.get(key));
    }

    /**
     * Set the result
     * @param key The var name
     * @param value The var value
     */
    public void setResult(String key, Object value) {
        vars.put(key, value);
    }

    /**
     * Get the platform this command originated from.
     * @return The platform
     */
    public Platform<?> getPlatform() {
        return platform;
    }

    /**
     * Test if this command matches the provided platform
     * @param testPlatform The platform to test
     * @return True, if the platform matches this one
     */
    public boolean forPlatform(Platform<?> testPlatform) {
        return StringUtils.equals(platform.getId(), testPlatform.getId());
    }

    /**
     * Concat the various constants into a single map.
     * The intention of this is for formatting of messages
     * @return A mapping of various useful objects, such as message contents, individual parameters,
     * and vars
     */
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
        return resolver.resolve(this);
    }

    public Mono<Map<String, Object>> getExtendedVariableMap() {
        return Mono.just(getVariableMapConstants());
    }

    public abstract Mono<Boolean> respond(String message);

    public abstract Mono<Boolean> alert(String message);
}

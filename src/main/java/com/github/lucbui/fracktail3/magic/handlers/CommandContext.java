package com.github.lucbui.fracktail3.magic.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.*;

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
    protected Command command;
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
     * Get the nth parameter, if it exists
     * @param idx The index of the parameter to retrieve
     * @return The parameter, or empty if the index doesn't match
     */
    public Optional<String> getNormalizedParameter(int idx) {
        if(idx < 0 || idx >= normalizedParameters.length) {
            return Optional.empty();
        }
        return Optional.of(normalizedParameters[idx]);
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
     * Get the command attempting to be used
     * @return The command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Set the command attempting to be used
     * @param command The command
     */
    public void setCommand(Command command) {
        this.command = command;
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

    /**
     * Get a possibly-asynchronously generated map of variables
     * @return Asynchronously-generated map of variables
     */
    public Mono<Map<String, Object>> getExtendedVariableMap() {
        return Mono.just(getVariableMapConstants());
    }

    /**
     * Get a ResourceBundle for this context
     * If no ResourceBundles exist, an empty Optional is returned.
     * @return The context of the bundle
     */
    public Optional<ResourceBundle> getResourceBundle() {
        //Enhancment: Per-command localization?
        if(config instanceof Localizable) {
            return ((Localizable) config).getBundleIfEnabled(getLocale());
        }
        return Optional.empty();
    }

    /**
     * Translate the key into some value
     * If no ResourceBundles exist, or no string matches the provided key, then an empty
     * optional is provided.
     * @param key The key to translate
     * @return The translated value
     */
    public Optional<String> translate(String key) {
        return getResourceBundle()
                .filter(bundle -> bundle.containsKey(key))
                .map(bundle -> bundle.getString(key));
    }

    /**
     * Translate the key into some value
     * If no ResourceBundles exist, or no string matches the provided key, then the provided default is returned.
     * @param key The key to translate
     * @param defaultStr The default value to provide
     * @return The translated value
     */
    public String translate(String key, String defaultStr) {
        return translate(key).orElse(defaultStr);
    }

    /**
     * Respond to the message which created this context
     * @param message The message to respond with
     * @return An asynchronous boolean indicating the message was sent
     */
    public abstract Mono<Boolean> respond(String message);

    /**
     * Respond using a localized message
     * @param key The key of the message
     * @param defaultMsg The text to show, if
     * @return An asynchronous boolean indicating the message was sent
     */
    public Mono<Boolean> respondLocalized(String key, String defaultMsg) {
        return respond(translate(key, defaultMsg));
    }

    /**
     * Respond using a localized message
     * If no ResourceBundles are found, or the key is not found, the key is the response
     * @param key The key of the message
     * @return An asynchronous boolean indicating the message was sent
     */
    public Mono<Boolean> respondLocalized(String key) {
        return respondLocalized(key, key);
    }

    /**
     * Alert the bot owner with a message
     * @param message The message to send to the bot owner
     * @return An asynchronous boolean indicating the message was sent
     */
    public abstract Mono<Boolean> alert(String message);
}

package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.ResolveFailedException;

import java.util.Locale;
import java.util.Optional;

/**
 * A resolver which uses a key to resolve a localized string
 */
public class I18NResolver implements Resolver<String> {
    private final String key;
    private final String defaultValue;

    /**
     * Resolve with the value for the provided key
     * @param key The key to use
     */
    public I18NResolver(String key) {
        this.key = key;
        this.defaultValue = null;
    }

    /**
     * Resolve with the value for the provided key, and default value if not present
     * @param key The key to use
     * @param defaultValue The default value to use, if the key does not resolve
     */
    public I18NResolver(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Get the I18N key
     * @return The I18N key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the default value, if present
     * @return The default value, if present
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String resolve(Config configuration, Locale locale) {
        Optional<String> candidate = configuration.getTextForKey(key, locale);
        if(defaultValue != null) {
            return candidate.orElse(defaultValue);
        } else {
            return candidate.orElseThrow(() ->
                    new ResolveFailedException("No key " + key + " for Locale " + locale));
        }
    }

    @Override
    public String toString() {
        return "I18NResolver{" +
                "key='" + key + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}

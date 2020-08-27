package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.ResolveFailedException;

import java.util.Locale;
import java.util.Optional;

public class I18NResolver implements Resolver<String> {
    private final String key;
    private final String defaultValue;

    public I18NResolver(String key) {
        this.key = key;
        this.defaultValue = null;
    }

    public I18NResolver(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

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

package com.github.lucbui.fracktail3.magic.config;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class GlobalConfiguration {
    private String i18nPath;

    public GlobalConfiguration(String i18nPath) {
        this.i18nPath = i18nPath;
    }

    public boolean hasI18nEnabled() {
        return i18nPath != null;
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(i18nPath, locale);
    }

    public Optional<String> getTextForKey(String key, Locale locale) {
        if(hasI18nEnabled()) {
            ResourceBundle bundle = getResourceBundle(locale);
            if (bundle.containsKey(key)) {
                return Optional.of(bundle.getString(key));
            }
        }
        return Optional.empty();
    }
}

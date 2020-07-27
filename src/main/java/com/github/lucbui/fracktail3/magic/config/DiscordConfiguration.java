package com.github.lucbui.fracktail3.magic.config;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class DiscordConfiguration extends GlobalConfiguration {
    private final GlobalConfiguration parent;
    private final String token;
    private final String prefix;

    public DiscordConfiguration(GlobalConfiguration parent, String token, String prefix, String i18nPath) {
       super(i18nPath);
       this.parent = parent;
       this.token = token;
       this.prefix = prefix;
    }

    public ResourceBundle getParentResourceBundle(Locale locale) {
        return parent.getResourceBundle(locale);
    }

    @Override
    public Optional<String> getTextForKey(String key, Locale locale) {
        if(hasI18nEnabled()) {
            ResourceBundle bundle = getResourceBundle(locale);
            if (bundle.containsKey(key)) {
                return Optional.of(bundle.getString(key));
            }
        }
        return parent == null ? Optional.empty() : parent.getTextForKey(key, locale);
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }
}

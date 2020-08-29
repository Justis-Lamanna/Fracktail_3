package com.github.lucbui.fracktail3.magic.config;

import com.github.lucbui.fracktail3.magic.resolver.Resolver;

import java.util.Locale;
import java.util.Optional;

public interface Config {
    Optional<String> getTextForKey(String key, Locale locale);

    default <T> T resolve(Resolver<T> resolver, Locale locale) {
        return resolver.resolve(this, locale);
    }
}

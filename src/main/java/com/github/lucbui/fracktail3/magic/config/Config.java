package com.github.lucbui.fracktail3.magic.config;

import java.util.Locale;
import java.util.Optional;

public interface Config {
    Optional<String> getTextForKey(java.lang.String key, Locale locale);
}

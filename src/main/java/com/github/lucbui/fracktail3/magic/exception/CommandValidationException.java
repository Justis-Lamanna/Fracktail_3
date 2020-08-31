package com.github.lucbui.fracktail3.magic.exception;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;

import java.util.Locale;

public class CommandValidationException extends RuntimeException {
    private final Resolver<String> message;

    protected CommandValidationException(Resolver<String> message) {
        this.message = message;
    }

    public static CommandValidationException literal(String message) {
        return new CommandValidationException(Resolver.identity(message));
    }

    public static CommandValidationException i18n(String key) {
        return new CommandValidationException(new I18NResolver(key));
    }

    public static CommandValidationException i18n(String key, String defaultValue) {
        return new CommandValidationException(new I18NResolver(key, defaultValue));
    }

    public String getMessage(Config config, Locale locale) {
        return message.resolve(config, locale);
    }
}

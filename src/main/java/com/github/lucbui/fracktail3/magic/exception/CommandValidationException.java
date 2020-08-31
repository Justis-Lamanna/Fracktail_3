package com.github.lucbui.fracktail3.magic.exception;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;

import java.util.Locale;

/**
 * Exception indicating validation of a command's parameter failed.
 * Unlike other exceptions, this type (and subclasses) will be caught by the Bot engine, and will respond with
 * the indicated message.
 */
public class CommandValidationException extends RuntimeException {
    private final Resolver<String> message;

    protected CommandValidationException(Resolver<String> message) {
        this.message = message;
    }

    /**
     * Generate an exception which responds with a literal message.
     * @param message The message to respond with.
     * @return A CommandValidationException which returns the supplied message.
     */
    public static CommandValidationException literal(String message) {
        return new CommandValidationException(Resolver.identity(message));
    }

    /**
     * Generate an exception, translated into the relevant language through ResourceBundles.
     * @param key The key to resolve into the localized message.
     * @return A CommandValidationException which returns the translated message.
     */
    public static CommandValidationException i18n(String key) {
        return new CommandValidationException(new I18NResolver(key));
    }

    /**
     * Generate an exception, translated into the relevant language through ResourceBundles.
     * A default value is provided if no bundle or key is present.
     * @param key The key to resolve into the localized message.
     * @return A CommandValidationException which returns the translated message.
     */
    public static CommandValidationException i18n(String key, String defaultValue) {
        return new CommandValidationException(new I18NResolver(key, defaultValue));
    }

    /**
     * Resolves and returns the message
     * @param config The configuration to use.
     * @param locale The locale to translate, if applicable.
     * @return The resolved message
     */
    public String getMessage(Config config, Locale locale) {
        return message.resolve(config, locale);
    }
}

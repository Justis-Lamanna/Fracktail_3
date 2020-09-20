package com.github.lucbui.fracktail3.magic.config;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;

import java.util.Locale;
import java.util.Optional;

/**
 * Describes common behavior for a Config
 */
public interface Config extends Validated {
    /**
     * Retrieve localized text for the specified key.
     * This is mainly for use with i18n, which is still a WIP.
     * @param key The key of the text to retrieve
     * @param locale The locale to filter for.
     * @return The localized string, or an empty optional if not found.
     */
    Optional<String> getTextForKey(String key, Locale locale);

    @Override
    default void validate(BotSpec spec) throws BotConfigurationException {
        //NOOP for now
    }
}

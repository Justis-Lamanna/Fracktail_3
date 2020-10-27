package com.github.lucbui.fracktail3.magic;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Marks an element as localizable
 */
public interface Localizable {
    /**
     * Get a ResourceBundle for a particular locale
     * @param locale The locale to use
     * @return The ResourceBundle for that locale
     */
    ResourceBundle getResourceBundle(Locale locale);

    /**
     * Return if localization is enabled or disabled
     * @return True, if localization is enabled
     */
    default boolean isLocalizationEnabled() {
        return true;
    }

    /**
     * Return a ResourceBundle, if localization is enabled
     * @param locale The locale to retrieve
     * @return The Bundle, or an empty optional if disabled
     */
    default Optional<ResourceBundle> getBundleIfEnabled(Locale locale) {
        return isLocalizationEnabled() ? Optional.of(getResourceBundle(locale)) : Optional.empty();
    }
}

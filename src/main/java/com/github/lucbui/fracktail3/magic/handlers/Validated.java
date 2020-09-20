package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;

/**
 * Marks a component as a candidate for validation
 */
public interface Validated {
    /**
     * Validate this component
     * @param spec The BotSpec being used
     * @throws BotConfigurationException Bot is incorrectly configured+
     */
    void validate(BotSpec spec) throws BotConfigurationException;

    /**
     * Validate an object, if it implements this interface
     * @param obj The object to validate
     * @param spec The spec to validate against
     * @throws BotConfigurationException Bot was incorrectly configured
     */
    static void validate(Object obj, BotSpec spec) throws BotConfigurationException {
        if(obj instanceof Validated) {
            ((Validated) obj).validate(spec);
        }
    }
}

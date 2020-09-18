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
}

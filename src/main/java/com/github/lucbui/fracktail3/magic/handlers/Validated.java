package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;

public interface Validated {
    void validate(BotSpec spec) throws BotConfigurationException;
}

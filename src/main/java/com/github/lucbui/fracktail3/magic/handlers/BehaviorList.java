package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;

public class BehaviorList implements Validated {
    private final CommandList commandList;

    public BehaviorList(CommandList commandList) {
        this.commandList = commandList;
    }

    public CommandList getCommandList() {
        return commandList;
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        commandList.validate(botSpec);
    }
}

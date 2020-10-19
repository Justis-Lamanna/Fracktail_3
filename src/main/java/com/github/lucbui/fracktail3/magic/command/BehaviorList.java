package com.github.lucbui.fracktail3.magic.command;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;

/**
 * A list of the behaviors this bot can handle
 */
public class BehaviorList implements Validated {
    private final CommandList commandList;

    /**
     * Initializes this BehaviorList
     * @param commandList The commands to use
     */
    public BehaviorList(CommandList commandList) {
        this.commandList = commandList;
    }

    /**
     * Get the list of commands used
     * @return The commands list
     */
    public CommandList getCommandList() {
        return commandList;
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        commandList.validate(botSpec);
    }
}

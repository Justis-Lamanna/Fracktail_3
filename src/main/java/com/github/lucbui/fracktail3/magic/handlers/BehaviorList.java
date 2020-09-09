package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.BotSpec;

public class BehaviorList {
    private final CommandList commandList;

    public BehaviorList(CommandList commandList) {
        this.commandList = commandList;
    }

    public CommandList getCommandList() {
        return commandList;
    }

    public void validate(BotSpec botSpec) {
        commandList.validate(botSpec);
    }
}

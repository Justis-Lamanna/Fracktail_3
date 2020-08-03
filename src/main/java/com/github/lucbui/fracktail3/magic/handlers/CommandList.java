package com.github.lucbui.fracktail3.magic.handlers;

import java.util.Collections;
import java.util.List;

public class CommandList {
    private final List<Command> commands;

    public CommandList(List<Command> commands) {
        this.commands = Collections.unmodifiableList(commands);
    }

    public List<Command> getCommands() {
        return commands;
    }
}

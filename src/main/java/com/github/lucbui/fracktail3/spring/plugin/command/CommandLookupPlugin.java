package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class CommandLookupPlugin implements Plugin {
    @Override
    public List<Command> addAdditionalCommands() {
        Command c = new Command.Builder("command-lookup-plugin.cmds")
                .withName("cmds")
                .withAction(new CommandListAction())
                .build();
        return Collections.singletonList(c);
    }
}

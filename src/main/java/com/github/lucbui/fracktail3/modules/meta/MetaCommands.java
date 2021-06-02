package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.spring.command.annotation.Parameter;
import com.github.lucbui.fracktail3.spring.command.annotation.Role;
import com.github.lucbui.fracktail3.spring.command.handler.FormattedStrings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class MetaCommands {
    @Autowired
    private CommandList commandList;

    @com.github.lucbui.fracktail3.spring.command.annotation.Command
    @Role("owner")
    public String addCmd(@Parameter(0) String name, @Parameter(1) String response) {
        Optional<Command> command = commandList.getCommandByName(name);
        if(command.isPresent()) {
            return "Sorry, command with name \"" + name + "\" already exists.";
        }

        Command newCmd = new Command.Builder("_custom_" + name)
                .withName(name)
                .withAction(new FormattedStrings.Action(response))
                .build();
        commandList.add(newCmd);
        return "Sure thing, I created that command for you.";
    }

    @com.github.lucbui.fracktail3.spring.command.annotation.Command
    @Role("owner")
    public String delCmd(@Parameter(0) String name) {
        Optional<Command> command = commandList.getCommandByName(name);
        if(command.isPresent()) {
            commandList.delete(command.get());
            return "Sure thing, I deleted that command.";
        }
        return "Sorry, command with name \"" + name + "\" does not exists.";
    }
}

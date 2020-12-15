package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.plugin.CommandPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * A plugin which injects the cmds and help commands.
 */
public class CommandLookupPlugin implements CommandPlugin {
    @Override
    public String getId() {
        return "command-lookup-plugin";
    }

    @Override
    public List<Command> addAdditionalCommands() {
        Command cmds = new Command.Builder("command-lookup-plugin.cmds")
                .withName("cmds")
                .withAction(new CommandListAction())
                .withHelp(FormattedString.from("Use !cmds to get a list of commands you can use."))
                .build();

        Command help = new Command.Builder("command-lookup-plugin.help")
                .withNames("help", "usage")
                .withAction(new CommandHelpAction())
                .withHelp(FormattedString.from("Use !help <command> to get usage on a particular command."))
                .build();
        return Arrays.asList(cmds, help);
    }
}

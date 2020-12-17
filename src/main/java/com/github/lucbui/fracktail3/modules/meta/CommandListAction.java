package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.annotation.Usage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * An action which provides a list of all usable commands
 * The command retrieves every command the user has access to, and all names and aliases the command has. These are
 * returned in alphabetical order.
 *
 * If the user has access to no commands (which shouldn't normally occur, since this command is being used and should
 * thus be returned), a specified string is returned
 */
@Component("cmds")
@Name("cmds")
@Usage("Use !cmds to get a list of commands you can use.")
public class CommandListAction implements CommandAction {
    private final String delimiter;
    private final FormattedString str;
    private final FormattedString noCommandsStr;

    /**
     * Initialize this action
     * Injected variable:
     * - commands: The constructed list of commands
     * @param delimiter A string to place in between each command in the listing
     * @param commandString The string to return containing one or more commands
     * @param noCommandsStr The string to return when no commands are found
     */
    public CommandListAction(String delimiter, FormattedString commandString, FormattedString noCommandsStr) {
        this.delimiter = delimiter;
        this.str = commandString;
        this.noCommandsStr = noCommandsStr;
    }

    /**
     * Initialize this action with defaults
     * Default delimiter is ", ".
     * Default text is "Commands are: {commands}".
     * Default no command text is "No commands are available"
     */
    public CommandListAction() {
        this(
                ", ",
                FormattedString.from("Commands are: {commands}"),
                FormattedString.from("No commands are available.")
        );
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        CommandLookupContext<?> cmdsCtx = new CommandLookupContext<>(context);
        List<Command> commands =
                context.getBot().getSpec().getCommandList().getCommands();

        return Flux.fromIterable(commands)
                .filterWhen(c -> c.matches(cmdsCtx))
                .flatMapIterable(com.github.lucbui.fracktail3.magic.command.Command::getNames)
                .sort()
                .collectList()
                .filter(CollectionUtils::isNotEmpty)
                .map(ls -> String.join(this.delimiter, ls))
                .flatMap(cmds -> str.getFor(context, Collections.singletonMap("commands", cmds)))
                .switchIfEmpty(noCommandsStr.getFor(context))
                .flatMap(context::respond);
    }
}

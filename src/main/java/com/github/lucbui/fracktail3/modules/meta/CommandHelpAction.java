package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.annotation.Usage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * An action which provides basic help when invoked
 * The command looks for the search string (parameter at index 0) among the set of commands the user is allowed
 * to use. If a matching command is found, its usage is returned. If none are found, a specified noCommandFound string
 * is returned.
 *
 * If no search string is provided, the attached command's help is returned instead, effectively showing how to use
 * this command.
 */
@Component("help")
@Name({"usage", "help"})
@Usage("Use !help <command> to get usage on a particular command.")
public class CommandHelpAction implements CommandAction {
    private final FormattedString noCommandFound;

    /**
     * Initialize this action with a FormattedString to respond with when no command was found.
     * Injected variable:
     * - search: The searched command
     * @param noCommandFound The string to respond with when the command is unknown
     */
    public CommandHelpAction(FormattedString noCommandFound) {
        this.noCommandFound = noCommandFound;
    }

    /**
     * Initialize this action
     * When no command is found, "I'm sorry, I don't know the command '{searched}'" is returned.
     */
    public CommandHelpAction() {
        this.noCommandFound = FormattedString.from("I'm sorry, I don't know the command ''{search}''.");
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        CommandLookupContext<?> cmdsCtx = new CommandLookupContext<>(context);
        List<Command> commands =
                context.getBot().getSpec().getCommandList().getCommands();

        return context.getParameters().getParameter(0)
                .map(searchString -> Flux.fromIterable(commands)
                        .filterWhen(c -> c.matches(cmdsCtx))
                        .filter(c -> c.getNames().contains(searchString))
                        .next()
                        .flatMap(c -> Mono.justOrEmpty(c.getHelp()))
                        .defaultIfEmpty(this.noCommandFound)
                        .flatMap(fs -> context.respond(fs, Collections.singletonMap("search", searchString)))
                )
                .orElse(context.respond(context.getCommand().getHelp()));
    }
}

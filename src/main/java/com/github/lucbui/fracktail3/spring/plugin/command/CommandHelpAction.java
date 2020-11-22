package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class CommandHelpAction implements CommandAction {
    private final FormattedString noCommandFound;

    public CommandHelpAction(FormattedString selfHelp, FormattedString noCommandFound) {
        this.noCommandFound = noCommandFound;
    }

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
                        .flatMap(fs -> context.respond(fs, Collections.singletonMap("search", searchString))))
                .orElse(context.respond(context.getCommand().getHelp()));
    }
}

package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.formatter.ICU4JDecoratorFormatter;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class CommandListAction implements CommandAction {
    private final String delimiter;
    private final FormattedString str;

    public CommandListAction(String delimiter, FormattedString commandString) {
        this.delimiter = delimiter;
        this.str = commandString;
    }

    public CommandListAction(FormattedString commandString) {
        this(", ", commandString);
    }

    public CommandListAction() {
        this(FormattedString.from("Commands are: {commands}", new ICU4JDecoratorFormatter()));
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
                .map(ls -> String.join(this.delimiter, ls))
                .flatMap(cmds -> str.getFor(context, Collections.singletonMap("commands", cmds)))
                .flatMap(context::respond);
    }
}

package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class CommandListAction implements CommandAction {
    private final String delimiter;
    private final FormattedString str;
    private final FormattedString noCommandsStr;

    public CommandListAction(String delimiter, FormattedString commandString, FormattedString noCommandsStr) {
        this.delimiter = delimiter;
        this.str = commandString;
        this.noCommandsStr = noCommandsStr;
    }

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

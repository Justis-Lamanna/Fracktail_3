package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelpCommand extends Command {
    private static final String ID = "help";

    public HelpCommand(String noCommandText, ContextFormatter contextFormatter) {
        super(ID, new HelpAction(noCommandText, contextFormatter));
    }

    public HelpCommand(String noCommandText) {
        this(noCommandText, ContextFormatter.DEFAULT);
    }

    public HelpCommand() {
        this("Unable to find command \"{result_command}\"");
    }

    private static class HelpAction implements Action {
        private final String noCommandText;
        private final ContextFormatter formatter;

        public HelpAction(String noCommandText, ContextFormatter formatter) {
            this.noCommandText = noCommandText;
            this.formatter = formatter;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            context.setResult("ignore_stateful_checks", true);
            return context.getNormalizedParameter(0)
                    .map(commandToLookup -> {
                        context.setResult("command", commandToLookup);
                        return Flux.fromIterable(bot.getSpec().getBehaviorList().getCommandList().getCommands())
                                        .filter(c -> c.getNames().contains(commandToLookup))
                                        .filterWhen(c -> c.passesFilter(bot, context))
                                        .singleOrEmpty()
                                        .map(Command::getHelp)
                                        .defaultIfEmpty(noCommandText)
                                        .flatMap(msg -> formatter.format(msg, context))
                                        .flatMap(context::respond);
                            })
                    .orElse(formatter.format(context.getCommand().getHelp(), context)
                            .flatMap(context::respond))
                    .then();
        }
    }
}

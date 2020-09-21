package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelpCommand extends Command {
    private static final String ID = "help";

    public HelpCommand(String noCommandText) {
        super(ID, new HelpAction(noCommandText));
    }

    public HelpCommand() {
        this("Unable to find command \"{result_command}\"");
    }

    private static class HelpAction implements Action {
        private final String noCommandText;

        public HelpAction(String noCommandText) {
            this.noCommandText = noCommandText;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            context.setResult("ignore_stateful_checks", true);
            return context.getNormalizedParameter(0)
                    .map(commandToLookup -> Flux.fromIterable(bot.getSpec().getBehaviorList().getCommandList().getCommands())
                            .filter(c -> c.getNames().contains(commandToLookup))
                            .filterWhen(c -> c.passesFilter(bot, context))
                            .singleOrEmpty()
                            .map(Command::getHelp)
                            .defaultIfEmpty(noCommandText)
                            .flatMap(msg -> {
                                context.setResult("command", commandToLookup);
                                return context.respond(msg);
                            }))
                    .orElse(context.respond(context.getCommand().getHelp()))
                    .then();
        }
    }
}

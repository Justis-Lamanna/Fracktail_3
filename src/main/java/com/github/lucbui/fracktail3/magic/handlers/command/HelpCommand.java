package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class HelpCommand extends Command {
    private static final String ID = "help";

    public HelpCommand(String noCommandText) {
        super(ID, new HelpAction(noCommandText));
    }

    public HelpCommand() {
        this("Unable to find command \"{command}\"");
    }

    private static class HelpAction implements Action {
        private final String noCommandText;

        public HelpAction(String noCommandText) {
            this.noCommandText = noCommandText;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            return context.getNormalizedParameter(0)
                    .map(commandToLookup -> {
                        Optional<Command> matching = bot.getSpec().getBehaviorList().getCommandList().getCommands().stream()
                                .filter(c -> c.getNames().contains(commandToLookup))
                                .findFirst();
                        return matching.map(Command::getHelp).map(context::respond)
                                .orElseGet(() -> {
                                    MessageFormat format = new MessageFormat(noCommandText, context.getLocale());
                                    return context.getExtendedVariableMap()
                                            .doOnNext(map -> map.put("command", commandToLookup))
                                            .map(format::format)
                                            .flatMap(context::respond);
                                });
                    })
                    .orElse(context.respond(context.getCommand().getHelp()))
                    .then();
        }
    }
}

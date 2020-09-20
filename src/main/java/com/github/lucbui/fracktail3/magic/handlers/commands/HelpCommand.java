package com.github.lucbui.fracktail3.magic.handlers.commands;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class HelpCommand extends Command {
    private static final String ID = "help";

    public HelpCommand() {
        super(ID, new HelpAction());
    }

    private static class HelpAction implements Action {
        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            if(context.getParameterCount() > 0) {
                String commandToLookup = context.getNormalizedParameters()[0];
                Optional<Command> matching = bot.getSpec().getBehaviorList().getCommandList().getCommands().stream()
                        .filter(c -> c.getNames().contains(commandToLookup))
                        .findFirst();
                return matching.map(Command::getHelp).map(context::respond)
                        .orElse(context.respond("Unable to find Command " + commandToLookup))
                        .then();
            } else {
                return context.respond(context.getCommand().getHelp())
                        .then();
            }
        }
    }
}

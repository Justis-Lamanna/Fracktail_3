package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class HelpCommand extends Command {
    public static final String ID = "help";
    public static final FormattedString NO_COMMANDS_TEXT = FormattedString.from("Unable to find command \"{result_command}\"");

    public HelpCommand(FormattedString noCommandText) {
        super(ID, new HelpAction(noCommandText));
    }

    public HelpCommand() {
        this(NO_COMMANDS_TEXT);
    }

    private static class HelpAction implements Action {
        private final FormattedString noCommandText;

        public HelpAction(FormattedString noCommandText) {
            this.noCommandText = noCommandText;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            return context.getNormalizedParameter(0)
                    .map(commandToLookup -> {
                        context.setResult("command", commandToLookup);
                        return Flux.fromIterable(bot.getSpec().getBehaviorList().getCommandList().getCommands())
                                        .filter(c -> c.getNames().contains(commandToLookup))
                                        .filterWhen(c -> c.passesFilter(bot, context))
                                        .next()
                                        .map(Command::getHelp)
                                        .flatMap(fs -> fs.getFor(context))
                                        .switchIfEmpty(noCommandText.getFor(context))
                                        .flatMap(context::respond);
                            })
                    .orElse(context.getCommand().getHelp().getFor(context)
                            .flatMap(context::respond))
                    .then();
        }
    }
}

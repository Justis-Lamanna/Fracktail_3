package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatters;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordPlatform;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Collectors;

public class CommandsCommand extends Command {
    public static final String ID = "commands";
    public static final String COMMANDS_LIST_TEST = "Commands are: {result_commands}";
    public static final String NO_COMMANDS_TEST = "You have access to no commands";

    public CommandsCommand(String message, String noCommandsStr, ContextFormatter contextFormatter) {
        super(ID, new CommandsAction(message, noCommandsStr, contextFormatter));
    }

    public CommandsCommand(String message) {
        super(ID, new CommandsAction(message, NO_COMMANDS_TEST, ContextFormatters.getDefault()));
    }

    public CommandsCommand() {
        super(ID, new CommandsAction(COMMANDS_LIST_TEST, NO_COMMANDS_TEST, ContextFormatters.getDefault()));
    }

    private static class CommandsAction implements Action {
        private final String message;
        private final String noCommandsMessage;
        private final ContextFormatter formatter;

        private CommandsAction(String message, String noCommandsMessage, ContextFormatter formatter) {
            this.message = message;
            this.noCommandsMessage = noCommandsMessage;
            this.formatter = formatter;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            Optional<String> prefixOpt = context.getPlatform() instanceof DiscordPlatform ?
                    Optional.ofNullable(((DiscordPlatform)context.getPlatform()).getConfig().getPrefix()) :
                    Optional.empty();
            return Flux.fromIterable(bot.getSpec().getBehaviorList().getCommandList().getCommands())
                    .filterWhen(c -> c.passesFilter(bot, context))
                    .map(c -> c.getNames().get(0))
                    .map(c -> prefixOpt.map(prefix -> prefix + c).orElse(c))
                    .sort()
                    .collect(Collectors.joining(", "))
                    .filter(StringUtils::isNotEmpty)
                    .flatMap(c -> {
                        context.setResult("commands", c);
                        return formatter.format(message, context);
                    })
                    .switchIfEmpty(formatter.format(noCommandsMessage, context))
                    .flatMap(context::respond)
                    .then();
        }
    }
}

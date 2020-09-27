package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
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
    public static final FormattedString COMMANDS_LIST_TEXT = FormattedString.from("Commands are: {result_commands}");
    public static final FormattedString NO_COMMANDS_TEXT = FormattedString.literal("You have access to no commands");

    public CommandsCommand(FormattedString message, FormattedString noCommandsMessage) {
        super(ID, new CommandsAction(message, noCommandsMessage));
    }

    public CommandsCommand() {
        this(COMMANDS_LIST_TEXT, NO_COMMANDS_TEXT);
    }

    private static class CommandsAction implements Action {
        private final FormattedString message;
        private final FormattedString noCommandsMessage;

        private CommandsAction(FormattedString message, FormattedString noCommandsMessage) {
            this.message = message;
            this.noCommandsMessage = noCommandsMessage;
        }

        @Override
        public Mono<Void> doAction(Bot bot, CommandContext context) {
            Optional<String> prefixOpt = context.getPlatform() instanceof DiscordPlatform ?
                    Optional.ofNullable(((DiscordPlatform)context.getPlatform()).getConfig().getPrefix()) :
                    Optional.empty();
            return Flux.fromIterable(bot.getSpec().getBehaviorList().getCommandList().getCommands())
                    .filterWhen(c -> c.passesFilter(bot, context))
                    .flatMap(c -> Flux.fromIterable(c.getNames()))
                    .distinct()
                    .map(c -> prefixOpt.map(prefix -> prefix + c).orElse(c))
                    .sort()
                    .collect(Collectors.joining(", "))
                    .filter(StringUtils::isNotEmpty)
                    .flatMap(c -> {
                        context.setResult("commands", c);
                        return message.getFor(context);
                    })
                    .switchIfEmpty(noCommandsMessage.getFor(context))
                    .flatMap(context::respond)
                    .then();
        }
    }
}

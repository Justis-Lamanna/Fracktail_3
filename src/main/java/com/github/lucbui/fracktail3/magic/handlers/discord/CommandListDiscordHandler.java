package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CommandListDiscordHandler implements DiscordHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListDiscordHandler.class);

    private static final Pattern SPACE_NOT_QUOTES = Pattern.compile("([^\"]\\S*|\".+?(?<!\\\\)\")\\s*");
    private static final Pattern DOUBLE_QUOTES_NO_BACKSLASH = Pattern.compile("(?<!\\\\)\"");

    private final CommandList commandList;

    public CommandListDiscordHandler(CommandList commandList) {
        this.commandList = commandList;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMessage().getContent())
                .zipWith(event.getGuild().map(Guild::getPreferredLocale).defaultIfEmpty(Locale.ENGLISH))
                .flatMap(tuple -> {
                    String msg = tuple.getT1();
                    Locale locale = tuple.getT2();
                    for(Command command : commandList.getCommands()) {
                        String[] names = Stream.concat(
                                Stream.of(command.getName().resolve(configuration, locale)),
                                command.getAliases().resolve(configuration, locale).stream()
                        ).toArray(String[]::new);
                        for(String name : names) {
                            if(StringUtils.startsWith(msg, configuration.getPrefix() + name)) {
                                DiscordContext context = new DiscordContext();
                                context.message = event;
                                context.locale = locale;
                                context.contents = msg;
                                context.command = name;
                                context.normalizedCommand = names[0];
                                context.parameters = StringUtils.removeStart(msg, configuration.getPrefix() + name).trim();
                                context.normalizedParameters = parseParameters(context.parameters);
                                LOGGER.debug("Executing command (User {} in {}):\n\tLocale: {}\n\tContents: {}\n\tCommand: {} (Normalized: {})\n\tParameters: {} (Normalized: {})",
                                        event.getMessage().getAuthor().map(User::getUsername).orElse("???"),
                                        event.getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                                        context.locale,
                                        context.contents,
                                        context.command, context.normalizedCommand,
                                        context.parameters, context.normalizedParameters);
                                return command.doDiscordAction(bot, configuration, context);
                            }
                        }
                    }
                    //No command found. Perform the "forElse" action, but for now, just return empty.
                    return Mono.empty();
                });
    }

    private String[] parseParameters(String paramString) {
        List<String> matches = new ArrayList<>();
        Matcher m = SPACE_NOT_QUOTES.matcher(paramString);
        while(m.find()) {
            String match = DOUBLE_QUOTES_NO_BACKSLASH.matcher(m.group(1)).replaceAll("");
            matches.add(match.trim());
        }
        return matches.toArray(new String[0]);
    }
}

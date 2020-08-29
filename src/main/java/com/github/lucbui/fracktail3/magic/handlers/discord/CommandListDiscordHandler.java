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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListDiscordHandler implements DiscordHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListDiscordHandler.class);

    private static final Pattern SPACE_NOT_QUOTES = Pattern.compile("([^\"]\\S*|\".+?(?<!\\\\)\")\\s*");
    private static final Pattern DOUBLE_QUOTES_NO_BACKSLASH = Pattern.compile("(?<!\\\\)\"");

    private final CommandList commandList;

    public CommandListDiscordHandler(CommandList commandList) {
        this.commandList = commandList;
    }

    public CommandList getCommandList() {
        return commandList;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event) {
        if(event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
            return Mono.empty();
        }
        return Mono.justOrEmpty(event.getMessage().getContent())
                .filter(s -> StringUtils.startsWith(s, configuration.getPrefix())) //Remove this?
                .map(msg -> new DiscordContext().setMessage(event).setContents(msg))
                .zipWith(event.getGuild().map(Guild::getPreferredLocale).defaultIfEmpty(Locale.ENGLISH), DiscordContext::setLocale)
                .zipWhen(ctx -> {
                    Map<String, List<Command.Resolved>> commands =
                            commandList.getCommandsByName(configuration, ctx.getLocale());

                    return Flux.fromIterable(commands.keySet())
                            .filter(name -> {
                                String msg = ctx.getContents();
                                if (commandList.isCaseSensitive()) {
                                    return StringUtils.startsWith(msg, configuration.getPrefix() + name);
                                } else {
                                    return StringUtils.startsWithIgnoreCase(msg, configuration.getPrefix() + name);
                                }
                            })
                            .flatMap(name -> Flux.fromIterable(commands.get(name)).zipWith(Mono.just(name)))
                            .filter(t -> t.getT1().isEnabled())
                            .filterWhen(t -> t.getT1().matchesRole(bot.getSpec(), ctx))
                            .singleOrEmpty()
                            .map(Optional::of).defaultIfEmpty(Optional.empty());
                }, (ctx, t) -> t.map(tuple -> ctx
                        .setResolvedCommand(tuple.getT1())
                        .setParameters(StringUtils.removeStart(ctx.getContents(), configuration.getPrefix() + tuple.getT2()))
                        .setNormalizedParameters(parseParameters(ctx.getParameters()))).orElse(ctx))
                .flatMap(ctx -> {
                    if(ctx.getResolvedCommand() == null) {
                        LOGGER.debug("Executing unknown command (User {} in {}):\n\tLocale: {}\n\tContents: {}",
                                ctx.getMessage().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                                ctx.getMessage().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                                ctx.getLocale(),
                                ctx.getContents());
                        return commandList.doOrElse(bot, ctx).thenReturn(true);
                    } else {
                        LOGGER.debug("Executing command (User {} in {}):\n\tLocale: {}\n\tContents: {}\n\tCommand: {}\n\tParameters: {} (Normalized: {})",
                                ctx.getMessage().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                                ctx.getMessage().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                                ctx.getLocale(),
                                ctx.getContents(),
                                ctx.getResolvedCommand().getId(),
                                ctx.getParameters(), ctx.getNormalizedParameters());
                        return ctx.getResolvedCommand().doAction(bot, ctx).thenReturn(true);
                    }
                })
                .then();
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

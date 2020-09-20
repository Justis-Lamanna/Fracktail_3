package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.exception.CommandValidationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
                .map(msg -> new DiscordContext(DiscordPlatform.INSTANCE, configuration, event))
                .zipWith(event.getGuild().map(Guild::getPreferredLocale).defaultIfEmpty(Locale.ENGLISH), (ctx, locale) -> {
                    ctx.setLocale(locale);
                    return ctx;
                })
                .zipWhen(ctx -> {
                    Map<String, List<Command>> commands = getCommandsByName(ctx);

                    return Flux.fromIterable(commands.keySet())
                            .filter(name -> StringUtils.startsWith(ctx.getContents(), configuration.getPrefix() + name))
                            .flatMap(name -> Flux.fromIterable(commands.get(name)).zipWith(Mono.just(name)))
                            .filterWhen(t -> t.getT1().passesFilter(bot, ctx))
                            .singleOrEmpty()
                            .map(Optional::of).defaultIfEmpty(Optional.empty());
                }, (ctx, t) -> t.map(tuple -> {
                    ctx.setCommand(tuple.getT1());
                    ctx.setParameters(StringUtils.removeStart(ctx.getContents(), configuration.getPrefix() + tuple.getT2()));
                    ctx.setNormalizedParameters(parseParameters(ctx.getParameters()));
                    LOGGER.debug("Executing command (User {} in {}):\n\tLocale: {}\n\tContents: {}\n\tCommand: {}\n\tParameters: {} (Normalized: {})",
                            ctx.getEvent().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                            ctx.getEvent().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                            ctx.getLocale(),
                            ctx.getContents(),
                            tuple.getT1().getId(),
                            ctx.getParameters(), ctx.getNormalizedParameters());
                    return tuple.getT1().doAction(bot, ctx)
                            .onErrorResume(CommandValidationException.class, ex -> {
                                String response = ex.getMessage();
                                return ctx.respondLocalized(response).then();
                            })
                            .onErrorResume(RuntimeException.class, ex -> {
                                LOGGER.warn("Encountered exception running command", ex);
                                return ctx.respond("Sorry, I encountered an exception. Please wait a few moments.")
                                        .then(ctx.alert("Encountered exception: " + ex.getMessage()))
                                        .then();
                            });
                }).orElseGet(() -> {
                    LOGGER.debug("Executing unknown command (User {} in {}):\n\tLocale: {}\n\tContents: {}",
                            ctx.getEvent().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                            ctx.getEvent().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                            ctx.getLocale(),
                            ctx.getContents());
                    return commandList.doOrElse(bot, ctx)
                            .onErrorResume(CommandValidationException.class, ex -> ctx.respondLocalized(ex.getMessage()).then())
                            .onErrorResume(RuntimeException.class, ex -> {
                                LOGGER.warn("Encountered exception running or-else command", ex);
                                return ctx.alert("Encountered exception: " + ex.getMessage()).then();
                            });
                }))
                .flatMap(m -> m); //Since zipping makes a Mono<Mono<Void>>, we need an identity map to unwrap.
    }

    private Map<String, List<Command>> getCommandsByName(DiscordContext ctx) {
        Map<String, List<Command>> commands = new HashMap<>(commandList.getNumberOfCommands());
        for(Command command : commandList.getCommands()) {
            List<String> names = command.getNames()
                    .stream()
                    .map(s -> ctx.translate(s, s))
                    .collect(Collectors.toList());
            for(String name : names) {
                commands.computeIfAbsent(name, key -> new ArrayList<>()).add(command);
            }
        }
        return commands;
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

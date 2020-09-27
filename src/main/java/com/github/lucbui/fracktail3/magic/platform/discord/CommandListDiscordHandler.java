package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.exception.CommandValidationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
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

    private final DiscordPlatform platform;
    private final CommandList commandList;
    private final DiscordLocaleResolver discordLocaleResolver;
    private final PreDiscordExecutionHandler preExecutionHandler;
    private final PostDiscordExecutionHandler postExecutionHandler;

    public CommandListDiscordHandler(
            DiscordPlatform platform,
            CommandList commandList,
            DiscordLocaleResolver discordLocaleResolver,
            PreDiscordExecutionHandler preExecutionHandler,
            PostDiscordExecutionHandler postExecutionHandler) {
        this.platform = platform;
        this.commandList = commandList;
        this.discordLocaleResolver = discordLocaleResolver;
        this.preExecutionHandler = preExecutionHandler;
        this.postExecutionHandler = postExecutionHandler;
    }

    public CommandListDiscordHandler(DiscordPlatform platform, CommandList commandList) {
        this(platform, commandList, new LocaleFromGuildResolver(), PreDiscordExecutionHandler.identity(), PostDiscordExecutionHandler.identity());
    }

    public CommandList getCommandList() {
        return commandList;
    }

    public DiscordLocaleResolver getDiscordLocaleResolver() {
        return discordLocaleResolver;
    }

    public PreDiscordExecutionHandler getPreExecutionHandler() {
        return preExecutionHandler;
    }

    public PostDiscordExecutionHandler getPostExecutionHandler() {
        return postExecutionHandler;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event) {
        if(event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
            return Mono.empty();
        }
        return Mono.justOrEmpty(event.getMessage().getContent())
                .filter(s -> StringUtils.startsWith(s, configuration.getPrefix())) //Remove this?
                .map(msg -> new DiscordContext(platform, configuration, event))
                .zipWith(discordLocaleResolver.getLocale(event), (ctx, locale) -> {
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
                }, (ctx, tupleOpt) -> {
                    tupleOpt.ifPresent(t -> {
                        ctx.setCommand(t.getT1());
                        ctx.setParameters(StringUtils.removeStart(ctx.getContents(), configuration.getPrefix() + t.getT2()));
                        ctx.setNormalizedParameters(parseParameters(ctx.getParameters()));
                    });

                    return ctx;
                })
                .flatMap(preExecutionHandler::beforeExecution)
                .flatMap(ctx -> {
                    boolean noCommandFound = ctx.getCommand() == null;
                    Mono<Void> action = noCommandFound ?
                            commandList.doOrElse(bot, ctx) :
                            ctx.getCommand().doAction(bot, ctx);
                    return action
                        .then(postExecutionHandler.afterExecution(ctx))
                        .onErrorResume(CommandValidationException.class, ex ->
                                ex.getFormattedMessage(ctx)
                                .flatMap(ctx::respond)
                                .then())
                        .onErrorResume(RuntimeException.class, ex -> postExecutionHandler.afterError(ctx, ex));
                });
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

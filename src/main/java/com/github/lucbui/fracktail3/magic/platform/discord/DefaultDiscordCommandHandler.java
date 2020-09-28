package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.exception.CommandValidationException;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDiscordCommandHandler implements DiscordCommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiscordCommandHandler.class);

    private static final Pattern SPACE_NOT_QUOTES = Pattern.compile("([^\"]\\S*|\".+?(?<!\\\\)\")\\s*");
    private static final Pattern DOUBLE_QUOTES_NO_BACKSLASH = Pattern.compile("(?<!\\\\)\"");

    private final DiscordPlatform platform;
    private final CommandList commandList;
    private final DiscordLocaleResolver discordLocaleResolver;
    private final PreDiscordExecutionHandler preExecutionHandler;
    private final PostDiscordExecutionHandler postExecutionHandler;

    public DefaultDiscordCommandHandler(
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

    public DefaultDiscordCommandHandler(DiscordPlatform platform, CommandList commandList) {
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
                .zipWhen(ctx -> getCommandsByName()
                        .filter(t -> StringUtils.startsWith(ctx.getContents(), configuration.getPrefix() + t.getT1()))
                        .filterWhen(t -> t.getT2().passesFilter(bot, ctx))
                        .singleOrEmpty()
                        .map(Optional::of).defaultIfEmpty(Optional.empty()), (ctx, tupleOpt) -> {
                    tupleOpt.ifPresent(t -> {
                        ctx.setCommand(t.getT2());
                        ctx.setParameters(StringUtils.removeStart(ctx.getContents(), configuration.getPrefix() + t.getT1()));
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

    private Flux<Tuple2<String, Command>> getCommandsByName() {
        return Flux.fromIterable(commandList.getCommands())
                .flatMap(command -> Flux.fromIterable(command.getNames())
                    .map(name -> Tuples.of(name, command)));
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

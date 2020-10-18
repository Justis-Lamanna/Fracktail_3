package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.DiscordCommandSearchContext;
import com.github.lucbui.fracktail3.discord.context.DiscordCommandUseContext;
import com.github.lucbui.fracktail3.magic.Bot;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default behavior to use when a message occurs
 */
public class DefaultDiscordCommandHandler implements DiscordCommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiscordCommandHandler.class);

    private static final Pattern SPACE_NOT_QUOTES = Pattern.compile("([^\"]\\S*|\".+?(?<!\\\\)\")\\s*");
    private static final Pattern DOUBLE_QUOTES_NO_BACKSLASH = Pattern.compile("(?<!\\\\)\"");

    private final CommandList commandList;
    private final DiscordLocaleResolver<MessageCreateEvent> discordLocaleResolver;
    private final DiscordExecutionHook executionHook;

    /**
     * Initialize this handler
     * @param commandList The command list to use
     * @param discordLocaleResolver The function to resolve the locale of a message
     * @param executionHook Code to execute before execution of a command
     */
    public DefaultDiscordCommandHandler(
            CommandList commandList,
            DiscordLocaleResolver<MessageCreateEvent> discordLocaleResolver,
            DiscordExecutionHook executionHook) {
        this.commandList = commandList;
        this.discordLocaleResolver = discordLocaleResolver;
        this.executionHook = executionHook;
    }

    /**
     * Initialize this handler
     * Default resolvers and handlers are used
     * @param commandList The command list to use
     */
    public DefaultDiscordCommandHandler(CommandList commandList) {
        this(commandList, new LocaleFromGuildResolver(), DiscordExecutionHook.identity());
    }

    public CommandList getCommandList() {
        return commandList;
    }

    public DiscordLocaleResolver<MessageCreateEvent> getDiscordLocaleResolver() {
        return discordLocaleResolver;
    }

    public DiscordExecutionHook getExecutionHook() {
        return executionHook;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordPlatform platform, MessageCreateEvent event) {
        if(event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
            return Mono.empty();
        }
        DiscordConfiguration configuration = platform.getConfig();
        String contents = event.getMessage().getContent();
        return Mono.justOrEmpty(contents)
                .filter(s -> StringUtils.startsWith(s, configuration.getPrefix())) //Remove this?
                .then(discordLocaleResolver.getLocale(event))
                .map(locale -> new DiscordCommandSearchContext(bot, platform, locale, event))
                .flatMap(ctx -> getCommandsByName()
                    .filter(t -> {
                        String commandName = t.getT1();
                        return StringUtils.startsWith(ctx.getPayload().getMessage().getContent(), configuration.getPrefix() + commandName);
                    })
                    .filterWhen(t -> {
                        Command cmd = t.getT2();
                        return cmd.matches(ctx);
                    })
                    .next()
                    .map(t -> {
                        String commandName = t.getT1();
                        Command cmd = t.getT2();
                        String rawParamString = StringUtils.removeStart(contents, configuration.getPrefix() + commandName);
                        return new DiscordCommandUseContext(ctx, cmd, rawParamString, parseParameters(rawParamString));
                    })
                    .flatMap(executionHook::beforeExecution)
                    .flatMap(dCtx -> {
                        Command cmd = dCtx.getCommand();
                        return cmd.doAction(dCtx)
                                .then(executionHook.afterExecution(dCtx))
                                .onErrorResume(RuntimeException.class, ex -> executionHook.afterError(dCtx, ex));
                    })
                    .switchIfEmpty(executionHook.onNoCommandFound(ctx))
                );
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

package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
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
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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

    public CommandList getCommandList() {
        return commandList;
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, MessageCreateEvent event) {
        if(event.getMessage().getAuthor().map(User::isBot).orElse(true)) {
            return Mono.empty();
        }
        return Mono.justOrEmpty(event.getMessage().getContent())
                .filter(s -> StringUtils.startsWith(s, configuration.getPrefix()))
                .zipWith(event.getGuild().map(Guild::getPreferredLocale).defaultIfEmpty(Locale.ENGLISH))
                .flatMap(tuple -> {
                    String msg = tuple.getT1();
                    Locale locale = tuple.getT2();

                    DiscordContext context = new DiscordContext();
                    context.setMessage(event);
                    context.setLocale(locale);
                    context.setContents(msg);

                    return Flux.fromIterable(commandList.getCommands())
                            .flatMap(c -> {
                                String[] names = Stream.concat(
                                        Stream.of(c.getName().resolve(configuration, locale)),
                                        c.getAliases().resolve(configuration, locale).stream()
                                ).toArray(String[]::new);
                                Optional<String> name = startsWithAny(msg, configuration.getPrefix(), names);
                                return Mono.justOrEmpty(name.map(used -> {
                                    context.setCommand(used);
                                    context.setNormalizedCommand(names[0]);
                                    context.setParameters(StringUtils.removeStart(msg, configuration.getPrefix() + used).trim());
                                    context.setNormalizedParameters(parseParameters(context.getParameters()));
                                    return Tuples.of(c, context);
                                }));
                            })
                            .filter(t -> t.getT1().isEnabled())
                            .filterWhen(t -> t.getT1().matchesRole(bot.getSpec(), t.getT2()))
                            .next()
                            .flatMap(t -> {
                                LOGGER.debug("Executing command (User {} in {}):\n\tLocale: {}\n\tContents: {}\n\tCommand: {} (Normalized: {})\n\tParameters: {} (Normalized: {})",
                                        context.getMessage().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                                        context.getMessage().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                                        context.getLocale(),
                                        context.getContents(),
                                        context.getCommand(), context.getNormalizedCommand(),
                                        context.getParameters(), context.getNormalizedParameters());
                                return t.getT1().doAction(bot, t.getT2()).thenReturn(true);
                            })
                            .switchIfEmpty(
                                    Mono.fromRunnable(() -> {
                                        LOGGER.debug("Executing unknown command (User {} in {}):\n\tLocale: {}\n\tContents: {}",
                                                context.getMessage().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                                                context.getMessage().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                                                context.getLocale(),
                                                context.getContents());
                                    }).then(commandList.doOrElse(bot, context).thenReturn(true))
                            );
                })
                .then();
    }

    private Mono<Boolean> canUseCommand(BotSpec botSpec, DiscordConfiguration configuration, Command command, DiscordContext ctx) {
        String[] names = Stream.concat(
                Stream.of(command.getName().resolve(configuration, ctx.getLocale())),
                command.getAliases().resolve(configuration, ctx.getLocale()).stream()
        ).toArray(String[]::new);
        Optional<String> name = startsWithAny(ctx.getContents(), configuration.getPrefix(), names);
        if(name.isPresent()) {

        }
        return Mono.just(false);
    }

    private Optional<String> startsWithAny(String value, String prefix, String... choices) {
        for(String choice : choices) {
            if (StringUtils.startsWith(value, prefix + choice)) {
                return Optional.of(choice);
            }
        }
        return Optional.empty();
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

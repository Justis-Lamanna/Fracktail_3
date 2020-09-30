package com.github.lucbui.fracktail3.discord.platform;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Logging Handler which can be used to log statements before and after handling
 */
public class LoggingDiscordExecutionHandler implements PreDiscordExecutionHandler, PostDiscordExecutionHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingDiscordExecutionHandler.class);

    @Override
    public Mono<DiscordContext> beforeExecution(DiscordContext ctx) {
        if(ctx.getCommand() == null) {
            LOGGER.debug("Executing unknown command (User {} in {}):\n\tLocale: {}\n\tContents: {}",
                    ctx.getEvent().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                    ctx.getEvent().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                    ctx.getLocale(),
                    ctx.getContents());
        } else {
            LOGGER.debug("Executing command (User {} in {}):\n\tLocale: {}\n\tContents: {}\n\tCommand: {}\n\tParameters: {} (Normalized: {})",
                    ctx.getEvent().getMessage().getAuthor().map(User::getUsername).orElse("???"),
                    ctx.getEvent().getGuildId().map(Snowflake::asString).map(s -> "Guild " + s).orElse("DMs"),
                    ctx.getLocale(),
                    ctx.getContents(),
                    ctx.getCommand().getId(),
                    ctx.getParameters(), ctx.getNormalizedParameters());
        }

        return Mono.just(ctx);
    }

    @Override
    public Mono<Void> afterExecution(DiscordContext ctx) {
        if(ctx.getCommand() == null) {
            LOGGER.debug("Execution of or-else behavior successful");
        } else {
            LOGGER.debug("Execution of command {} successful", ctx.getCommand().getId());
        }
        return Mono.empty();
    }

    @Override
    public Mono<Void> afterError(DiscordContext ctx, Exception exception) {
        boolean noCommandFound = ctx.getCommand() == null;
        LOGGER.warn(noCommandFound ?
                "Encountered exception running or-else command" :
                "Encountered exception running command", exception);
        return Mono.empty();
    }
}

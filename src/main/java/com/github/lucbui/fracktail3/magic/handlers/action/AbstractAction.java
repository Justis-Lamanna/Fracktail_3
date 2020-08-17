package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.NamedParameters;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public abstract class AbstractAction implements Action {
    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context, NamedParameters params) {
        if(context.isDiscord()) {
            return doDiscordAction(bot, (DiscordContext)context, params);
        } else {
            return doUnknownAction(bot, context, params);
        }
    }

    protected abstract Mono<Void> doDiscordAction(Bot bot, DiscordContext ctx, NamedParameters params);

    protected Mono<Void> doUnknownAction(Bot bot, CommandContext context, NamedParameters params) {
        LoggerFactory.getLogger(getClass()).info("Attempted action {} with context {}. Unable to handle, so nothing happened.", getClass().getSimpleName(), context.getClass().getSimpleName());
        return Mono.empty();
    }
}

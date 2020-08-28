package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public abstract class AbstractAction implements Action {
    @Override
    public Mono<Void> doAction(BotSpec botSpec, CommandContext context) {
        if(context.isDiscord()) {
            return doDiscordAction(botSpec, (DiscordContext)context);
        } else {
            return doUnknownAction(botSpec, context);
        }
    }

    protected abstract Mono<Void> doDiscordAction(BotSpec botSpec, DiscordContext ctx);

    protected Mono<Void> doUnknownAction(BotSpec botSpec, CommandContext context) {
        LoggerFactory.getLogger(getClass()).info("Attempted action {} with context {}. Unable to handle, so nothing happened.", getClass().getSimpleName(), context.getClass().getSimpleName());
        return Mono.empty();
    }
}

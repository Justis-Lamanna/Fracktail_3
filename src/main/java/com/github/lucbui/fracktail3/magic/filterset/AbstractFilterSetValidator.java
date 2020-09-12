package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import reactor.core.publisher.Mono;

public abstract class AbstractFilterSetValidator implements FilterSetValidator {
    @Override
    public Mono<Boolean> validateInRole(BotSpec botSpec, CommandContext ctx) {
        if(ctx.isDiscord()) {
            return validateInDiscordRole(botSpec, (DiscordContext)ctx);
        } else {
            return validateInUnknownRole(botSpec, ctx);
        }
    }

    protected abstract Mono<Boolean> validateInUnknownRole(BotSpec botSpec, CommandContext ctx);

    protected abstract Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx);
}

package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import reactor.core.publisher.Mono;

public abstract class AbstractRolesetValidator implements RolesetValidator {
    @Override
    public Mono<Boolean> validateInRole(Bot bot, CommandContext ctx) {
        if(ctx.isDiscord()) {
            return validateInDiscordRole(bot, (DiscordContext)ctx);
        } else {
            return validateInUnknownRole(bot, ctx);
        }
    }

    protected abstract Mono<Boolean> validateInUnknownRole(Bot bot, CommandContext ctx);

    protected abstract Mono<Boolean> validateInDiscordRole(Bot bot, DiscordContext ctx);
}

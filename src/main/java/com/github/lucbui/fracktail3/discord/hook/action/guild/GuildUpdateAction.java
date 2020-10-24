package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.GuildUpdateContext;
import reactor.core.publisher.Mono;

public interface GuildUpdateAction {
    Mono<Void> doAction(GuildUpdateContext ctx);
}
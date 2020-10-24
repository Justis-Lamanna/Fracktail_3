package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.GuildDeleteContext;
import reactor.core.publisher.Mono;

public interface GuildDeleteAction {
    Mono<Void> doAction(GuildDeleteContext ctx);
}
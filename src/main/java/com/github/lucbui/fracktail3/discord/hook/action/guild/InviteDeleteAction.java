package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.InviteDeleteContext;
import reactor.core.publisher.Mono;

public interface InviteDeleteAction {
    Mono<Void> doAction(InviteDeleteContext ctx);
}
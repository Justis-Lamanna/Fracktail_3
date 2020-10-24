package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.InviteCreateContext;
import reactor.core.publisher.Mono;

public interface InviteCreateAction {
    Mono<Void> doAction(InviteCreateContext ctx);
}
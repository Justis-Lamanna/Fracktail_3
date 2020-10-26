package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.InviteCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface InviteCreateAction {
    Mono<Void> doAction(InviteCreateContext ctx);

	default Mono<Boolean> guard(InviteCreateContext ctx){ return Mono.just(true); }
}
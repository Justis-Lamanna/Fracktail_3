package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.InviteDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface InviteDeleteAction {
    Mono<Void> doAction(InviteDeleteContext ctx);

	default Mono<Boolean> guard(InviteDeleteContext ctx){ return Mono.just(true); }
}
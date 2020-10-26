package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberLeaveContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MemberLeaveAction {
    Mono<Void> doAction(MemberLeaveContext ctx);

	default Mono<Boolean> guard(MemberLeaveContext ctx){ return Mono.just(true); }
}
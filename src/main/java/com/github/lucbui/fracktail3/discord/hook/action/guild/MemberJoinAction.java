package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberJoinContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MemberJoinAction {
    Mono<Void> doAction(MemberJoinContext ctx);

	default Mono<Boolean> guard(MemberJoinContext ctx){ return Mono.just(true); }
}
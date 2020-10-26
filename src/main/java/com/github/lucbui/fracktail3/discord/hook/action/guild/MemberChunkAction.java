package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberChunkContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MemberChunkAction {
    Mono<Void> doAction(MemberChunkContext ctx);

	default Mono<Boolean> guard(MemberChunkContext ctx){ return Mono.just(true); }
}
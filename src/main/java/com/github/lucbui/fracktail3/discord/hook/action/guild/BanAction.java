package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.BanContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BanAction {
    Mono<Void> doAction(BanContext ctx);

	default Mono<Boolean> guard(BanContext ctx){ return Mono.just(true); }
}
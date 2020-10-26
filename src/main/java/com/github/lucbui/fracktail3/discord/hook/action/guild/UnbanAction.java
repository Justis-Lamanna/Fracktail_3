package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.UnbanContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UnbanAction {
    Mono<Void> doAction(UnbanContext ctx);

	default Mono<Boolean> guard(UnbanContext ctx){ return Mono.just(true); }
}
package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ReadyContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReadyAction {
    Mono<Void> doAction(ReadyContext ctx);

	default Mono<Boolean> guard(ReadyContext ctx){ return Mono.just(true); }
}
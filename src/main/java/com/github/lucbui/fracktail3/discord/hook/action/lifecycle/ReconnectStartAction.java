package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ReconnectStartContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReconnectStartAction {
    Mono<Void> doAction(ReconnectStartContext ctx);

	default Mono<Boolean> guard(ReconnectStartContext ctx){ return Mono.just(true); }
}
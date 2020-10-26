package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ReconnectContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReconnectAction {
    Mono<Void> doAction(ReconnectContext ctx);

	default Mono<Boolean> guard(ReconnectContext ctx){ return Mono.just(true); }
}
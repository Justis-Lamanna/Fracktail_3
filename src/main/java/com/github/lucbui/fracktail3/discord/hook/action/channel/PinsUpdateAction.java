package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.PinsUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface PinsUpdateAction {
    Mono<Void> doAction(PinsUpdateContext ctx);

	default Mono<Boolean> guard(PinsUpdateContext ctx){ return Mono.just(true); }
}
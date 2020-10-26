package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.MessageUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MessageUpdateAction {
    Mono<Void> doAction(MessageUpdateContext ctx);

	default Mono<Boolean> guard(MessageUpdateContext ctx){ return Mono.just(true); }
}
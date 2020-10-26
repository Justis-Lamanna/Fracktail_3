package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TypingStartContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface TypingStartAction {
    Mono<Void> doAction(TypingStartContext ctx);

	default Mono<Boolean> guard(TypingStartContext ctx){ return Mono.just(true); }
}
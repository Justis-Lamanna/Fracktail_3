package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionAddContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReactionAddAction {
    Mono<Void> doAction(ReactionAddContext ctx);

	default Mono<Boolean> guard(ReactionAddContext ctx){ return Mono.just(true); }
}
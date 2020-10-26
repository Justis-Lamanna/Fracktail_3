package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.CategoryUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CategoryUpdateAction {
    Mono<Void> doAction(CategoryUpdateContext ctx);

	default Mono<Boolean> guard(CategoryUpdateContext ctx){ return Mono.just(true); }
}
package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.CategoryDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CategoryDeleteAction {
    Mono<Void> doAction(CategoryDeleteContext ctx);

	default Mono<Boolean> guard(CategoryDeleteContext ctx){ return Mono.just(true); }
}
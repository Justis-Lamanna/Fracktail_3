package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.WebhooksUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface WebhooksUpdateAction {
    Mono<Void> doAction(WebhooksUpdateContext ctx);

	default Mono<Boolean> guard(WebhooksUpdateContext ctx){ return Mono.just(true); }
}
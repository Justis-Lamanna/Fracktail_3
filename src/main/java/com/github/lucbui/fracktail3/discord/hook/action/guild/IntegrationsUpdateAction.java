package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.IntegrationsUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IntegrationsUpdateAction {
    Mono<Void> doAction(IntegrationsUpdateContext ctx);

	default Mono<Boolean> guard(IntegrationsUpdateContext ctx){ return Mono.just(true); }
}
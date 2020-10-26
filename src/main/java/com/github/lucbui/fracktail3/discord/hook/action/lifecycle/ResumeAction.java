package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ResumeContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ResumeAction {
    Mono<Void> doAction(ResumeContext ctx);

	default Mono<Boolean> guard(ResumeContext ctx){ return Mono.just(true); }
}
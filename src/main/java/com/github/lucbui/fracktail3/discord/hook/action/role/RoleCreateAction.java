package com.github.lucbui.fracktail3.discord.hook.action.role;

import com.github.lucbui.fracktail3.discord.hook.context.role.RoleCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface RoleCreateAction {
    Mono<Void> doAction(RoleCreateContext ctx);

	default Mono<Boolean> guard(RoleCreateContext ctx){ return Mono.just(true); }
}
package com.github.lucbui.fracktail3.discord.hook.action.role;

import com.github.lucbui.fracktail3.discord.hook.context.role.RoleDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface RoleDeleteAction {
    Mono<Void> doAction(RoleDeleteContext ctx);

	default Mono<Boolean> guard(RoleDeleteContext ctx){ return Mono.just(true); }
}
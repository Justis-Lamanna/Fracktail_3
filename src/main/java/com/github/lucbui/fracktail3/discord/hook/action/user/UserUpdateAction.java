package com.github.lucbui.fracktail3.discord.hook.action.user;

import com.github.lucbui.fracktail3.discord.hook.context.user.UserUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UserUpdateAction {
    Mono<Void> doAction(UserUpdateContext ctx);

	default Mono<Boolean> guard(UserUpdateContext ctx){ return Mono.just(true); }
}
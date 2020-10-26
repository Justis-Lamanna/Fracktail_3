package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.GuildCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GuildCreateAction {
    Mono<Void> doAction(GuildCreateContext ctx);

	default Mono<Boolean> guard(GuildCreateContext ctx){ return Mono.just(true); }
}
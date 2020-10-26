package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface StoreChannelCreateAction {
    Mono<Void> doAction(StoreChannelCreateContext ctx);

	default Mono<Boolean> guard(StoreChannelCreateContext ctx){ return Mono.just(true); }
}
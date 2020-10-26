package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface StoreChannelDeleteAction {
    Mono<Void> doAction(StoreChannelDeleteContext ctx);

	default Mono<Boolean> guard(StoreChannelDeleteContext ctx){ return Mono.just(true); }
}
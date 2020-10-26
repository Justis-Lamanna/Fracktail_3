package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface StoreChannelUpdateAction {
    Mono<Void> doAction(StoreChannelUpdateContext ctx);

	default Mono<Boolean> guard(StoreChannelUpdateContext ctx){ return Mono.just(true); }
}
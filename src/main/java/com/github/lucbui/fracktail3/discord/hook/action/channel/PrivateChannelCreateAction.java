package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.PrivateChannelCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface PrivateChannelCreateAction {
    Mono<Void> doAction(PrivateChannelCreateContext ctx);

	default Mono<Boolean> guard(PrivateChannelCreateContext ctx){ return Mono.just(true); }
}
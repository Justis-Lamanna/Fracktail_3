package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.PrivateChannelDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface PrivateChannelDeleteAction {
    Mono<Void> doAction(PrivateChannelDeleteContext ctx);

	default Mono<Boolean> guard(PrivateChannelDeleteContext ctx){ return Mono.just(true); }
}
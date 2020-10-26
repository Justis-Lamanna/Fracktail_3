package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TextChannelCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface TextChannelCreateAction {
    Mono<Void> doAction(TextChannelCreateContext ctx);

	default Mono<Boolean> guard(TextChannelCreateContext ctx){ return Mono.just(true); }
}
package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TextChannelUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface TextChannelUpdateAction {
    Mono<Void> doAction(TextChannelUpdateContext ctx);

	default Mono<Boolean> guard(TextChannelUpdateContext ctx){ return Mono.just(true); }
}
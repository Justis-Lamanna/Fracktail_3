package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.NewsChannelUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface NewsChannelUpdateAction {
    Mono<Void> doAction(NewsChannelUpdateContext ctx);

	default Mono<Boolean> guard(NewsChannelUpdateContext ctx){ return Mono.just(true); }
}
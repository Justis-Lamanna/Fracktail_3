package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.NewsChannelDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface NewsChannelDeleteAction {
    Mono<Void> doAction(NewsChannelDeleteContext ctx);

	default Mono<Boolean> guard(NewsChannelDeleteContext ctx){ return Mono.just(true); }
}
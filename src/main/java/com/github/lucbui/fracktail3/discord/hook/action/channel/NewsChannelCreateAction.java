package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.NewsChannelCreateContext;
import reactor.core.publisher.Mono;

public interface NewsChannelCreateAction {
    Mono<Void> doAction(NewsChannelCreateContext ctx);
}
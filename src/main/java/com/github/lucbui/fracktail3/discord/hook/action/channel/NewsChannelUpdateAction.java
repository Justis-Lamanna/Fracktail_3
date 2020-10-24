package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.NewsChannelUpdateContext;
import reactor.core.publisher.Mono;

public interface NewsChannelUpdateAction {
    Mono<Void> doAction(NewsChannelUpdateContext ctx);
}
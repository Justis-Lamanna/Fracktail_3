package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.NewsChannelDeleteContext;
import reactor.core.publisher.Mono;

public interface NewsChannelDeleteAction {
    Mono<Void> doAction(NewsChannelDeleteContext ctx);
}
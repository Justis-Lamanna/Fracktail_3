package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelCreateContext;
import reactor.core.publisher.Mono;

public interface StoreChannelCreateAction {
    Mono<Void> doAction(StoreChannelCreateContext ctx);
}
package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelUpdateContext;
import reactor.core.publisher.Mono;

public interface StoreChannelUpdateAction {
    Mono<Void> doAction(StoreChannelUpdateContext ctx);
}
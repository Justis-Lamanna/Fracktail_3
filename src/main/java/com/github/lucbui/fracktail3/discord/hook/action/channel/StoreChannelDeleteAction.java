package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.StoreChannelDeleteContext;
import reactor.core.publisher.Mono;

public interface StoreChannelDeleteAction {
    Mono<Void> doAction(StoreChannelDeleteContext ctx);
}
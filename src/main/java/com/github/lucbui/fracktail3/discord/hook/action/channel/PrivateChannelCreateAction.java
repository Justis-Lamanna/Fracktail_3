package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.PrivateChannelCreateContext;
import reactor.core.publisher.Mono;

public interface PrivateChannelCreateAction {
    Mono<Void> doAction(PrivateChannelCreateContext ctx);
}
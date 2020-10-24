package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TextChannelCreateContext;
import reactor.core.publisher.Mono;

public interface TextChannelCreateAction {
    Mono<Void> doAction(TextChannelCreateContext ctx);
}
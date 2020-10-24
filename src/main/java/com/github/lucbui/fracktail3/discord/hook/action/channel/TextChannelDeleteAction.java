package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TextChannelDeleteContext;
import reactor.core.publisher.Mono;

public interface TextChannelDeleteAction {
    Mono<Void> doAction(TextChannelDeleteContext ctx);
}
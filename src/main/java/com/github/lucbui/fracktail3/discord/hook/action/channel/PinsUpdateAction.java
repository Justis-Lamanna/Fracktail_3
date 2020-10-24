package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.PinsUpdateContext;
import reactor.core.publisher.Mono;

public interface PinsUpdateAction {
    Mono<Void> doAction(PinsUpdateContext ctx);
}
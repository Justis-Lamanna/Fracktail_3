package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.TypingStartContext;
import reactor.core.publisher.Mono;

public interface TypingStartAction {
    Mono<Void> doAction(TypingStartContext ctx);
}
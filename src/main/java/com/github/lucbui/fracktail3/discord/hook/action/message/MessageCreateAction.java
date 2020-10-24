package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.MessageCreateContext;
import reactor.core.publisher.Mono;

public interface MessageCreateAction {
    Mono<Void> doAction(MessageCreateContext ctx);
}
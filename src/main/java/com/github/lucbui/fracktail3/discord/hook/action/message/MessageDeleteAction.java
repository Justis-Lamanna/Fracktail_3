package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.MessageDeleteContext;
import reactor.core.publisher.Mono;

public interface MessageDeleteAction {
    Mono<Void> doAction(MessageDeleteContext ctx);
}
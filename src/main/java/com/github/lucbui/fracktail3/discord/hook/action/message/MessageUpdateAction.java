package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.MessageUpdateContext;
import reactor.core.publisher.Mono;

public interface MessageUpdateAction {
    Mono<Void> doAction(MessageUpdateContext ctx);
}
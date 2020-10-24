package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.MessageBulkDeleteContext;
import reactor.core.publisher.Mono;

public interface MessageBulkDeleteAction {
    Mono<Void> doAction(MessageBulkDeleteContext ctx);
}
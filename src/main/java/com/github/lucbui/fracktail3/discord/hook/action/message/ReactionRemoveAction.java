package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionRemoveContext;
import reactor.core.publisher.Mono;

public interface ReactionRemoveAction {
    Mono<Void> doAction(ReactionRemoveContext ctx);
}
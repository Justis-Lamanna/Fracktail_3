package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionRemoveAllContext;
import reactor.core.publisher.Mono;

public interface ReactionRemoveAllAction {
    Mono<Void> doAction(ReactionRemoveAllContext ctx);
}
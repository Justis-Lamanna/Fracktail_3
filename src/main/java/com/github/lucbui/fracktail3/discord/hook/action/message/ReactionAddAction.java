package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionAddContext;
import reactor.core.publisher.Mono;

public interface ReactionAddAction {
    Mono<Void> doAction(ReactionAddContext ctx);
}
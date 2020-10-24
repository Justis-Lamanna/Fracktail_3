package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionRemoveEmojiContext;
import reactor.core.publisher.Mono;

public interface ReactionRemoveEmojiAction {
    Mono<Void> doAction(ReactionRemoveEmojiContext ctx);
}
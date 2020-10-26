package com.github.lucbui.fracktail3.discord.hook.action.message;

import com.github.lucbui.fracktail3.discord.hook.context.message.ReactionRemoveEmojiContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ReactionRemoveEmojiAction {
    Mono<Void> doAction(ReactionRemoveEmojiContext ctx);

	default Mono<Boolean> guard(ReactionRemoveEmojiContext ctx){ return Mono.just(true); }
}
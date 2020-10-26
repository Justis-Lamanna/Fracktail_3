package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.EmojiUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface EmojisUpdateAction {
    Mono<Void> doAction(EmojiUpdateContext ctx);

	default Mono<Boolean> guard(EmojiUpdateContext ctx){ return Mono.just(true); }
}
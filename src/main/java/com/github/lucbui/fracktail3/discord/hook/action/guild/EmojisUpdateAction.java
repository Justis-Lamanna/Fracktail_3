package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.EmojiUpdateContext;
import reactor.core.publisher.Mono;

public interface EmojisUpdateAction {
    Mono<Void> doAction(EmojiUpdateContext ctx);
}
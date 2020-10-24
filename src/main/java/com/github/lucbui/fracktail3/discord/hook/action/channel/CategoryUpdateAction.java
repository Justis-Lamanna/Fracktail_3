package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.CategoryUpdateContext;
import reactor.core.publisher.Mono;

public interface CategoryUpdateAction {
    Mono<Void> doAction(CategoryUpdateContext ctx);
}
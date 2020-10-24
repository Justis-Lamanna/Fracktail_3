package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.CategoryCreateContext;
import reactor.core.publisher.Mono;

public interface CategoryCreateAction {
    Mono<Void> doAction(CategoryCreateContext ctx);
}
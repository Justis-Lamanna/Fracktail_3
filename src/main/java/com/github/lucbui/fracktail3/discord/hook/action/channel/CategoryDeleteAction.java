package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.CategoryDeleteContext;
import reactor.core.publisher.Mono;

public interface CategoryDeleteAction {
    Mono<Void> doAction(CategoryDeleteContext ctx);
}
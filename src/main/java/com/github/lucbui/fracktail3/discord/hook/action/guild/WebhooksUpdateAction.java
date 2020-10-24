package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.WebhooksUpdateContext;
import reactor.core.publisher.Mono;

public interface WebhooksUpdateAction {
    Mono<Void> doAction(WebhooksUpdateContext ctx);
}
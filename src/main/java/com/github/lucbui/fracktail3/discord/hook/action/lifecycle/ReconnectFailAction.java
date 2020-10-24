package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ReconnectFailContext;
import reactor.core.publisher.Mono;

public interface ReconnectFailAction {
    Mono<Void> doAction(ReconnectFailContext ctx);
}
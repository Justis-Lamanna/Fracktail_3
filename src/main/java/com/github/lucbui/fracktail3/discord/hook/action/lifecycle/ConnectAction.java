package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.ConnectContext;
import reactor.core.publisher.Mono;

public interface ConnectAction {
    Mono<Void> doAction(ConnectContext ctx);
}
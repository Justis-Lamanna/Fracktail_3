package com.github.lucbui.fracktail3.discord.hook.action.lifecycle;

import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.DisconnectContext;
import reactor.core.publisher.Mono;

public interface DisconnectAction {
    Mono<Void> doAction(DisconnectContext ctx);
}
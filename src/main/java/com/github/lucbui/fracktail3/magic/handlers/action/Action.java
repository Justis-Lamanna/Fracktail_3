package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public interface Action {
    Mono<Void> doAction(Bot bot, CommandContext context);
}

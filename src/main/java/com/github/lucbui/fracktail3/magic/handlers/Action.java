package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandContext;
import reactor.core.publisher.Mono;

public interface Action {
    Mono<Void> doAction(Bot bot, CommandContext context, NamedParameters params);
}

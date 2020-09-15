package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public interface Filter {
    Mono<Boolean> matches(Bot bot, CommandContext ctx);

    static Filter identity(boolean value) {
        return (bot, ctx) -> Mono.just(value);
    }
}

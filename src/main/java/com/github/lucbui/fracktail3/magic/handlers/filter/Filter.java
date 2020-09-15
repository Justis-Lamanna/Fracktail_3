package com.github.lucbui.fracktail3.magic.handlers.filter;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public interface Filter {
    Mono<Boolean> matches(Bot bot, CommandContext ctx);
}

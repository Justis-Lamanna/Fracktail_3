package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

public interface ContextFormatter {
    Mono<String> format(String raw, CommandContext ctx);

    static ContextFormatter identity() {
        return (raw, ctx) -> Mono.just(raw);
    }
}

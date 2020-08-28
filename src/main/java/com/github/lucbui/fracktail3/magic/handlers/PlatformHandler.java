package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.BotSpec;
import reactor.core.publisher.Mono;

public interface PlatformHandler {
    Mono<Boolean> start(BotSpec botSpec);
    Mono<Boolean> stop(BotSpec botSpec);
}

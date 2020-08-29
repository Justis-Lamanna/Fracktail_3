package com.github.lucbui.fracktail3.magic.handlers.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

public interface PlatformHandler {
    Mono<Boolean> start(Bot bot);
    Mono<Boolean> stop(Bot bot);
}

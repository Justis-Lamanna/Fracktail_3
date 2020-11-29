package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

public class TestPlatform implements Platform {
    @Override
    public Config getConfig() {
        return null;
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        return null;
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        return null;
    }

    @Override
    public String getId() {
        return "test-platform";
    }
}

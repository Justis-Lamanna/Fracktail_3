package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

public class TestContext extends CommandContext {
    public TestContext(String message) {
        super(new TestConfig(), message);
    }

    @Override
    public Mono<Boolean> respond(String message) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> alert(String message) {
        return Mono.just(true);
    }

    private static class TestConfig implements Config {

    }
}

package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.guards.channel.Channelset;
import com.github.lucbui.fracktail3.magic.guards.user.Userset;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Context which can be used for testing
 */
public class TestContext extends CommandContext {
    /**
     * Initialize a context with some message
     * @param message The message to use
     */
    public TestContext(String message) {
        this(new TestConfig(), message);
    }

    /**
     * Initialize a context with a config and message
     * @param config The config to use
     * @param message The message to use
     */
    public TestContext(Config config, String message) {
        super(config, message);
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
        @Override
        public Optional<? extends Userset> getUserset(String id) {
            return Optional.empty();
        }

        @Override
        public Optional<? extends Channelset> getChannelset(String id) {
            return Optional.empty();
        }
    }
}

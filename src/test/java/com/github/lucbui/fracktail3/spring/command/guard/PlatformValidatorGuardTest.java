package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class PlatformValidatorGuardTest extends BaseFracktailTest {

    @BeforeEach
    void setup() {
        super.parentSetup();
    }

    @Test
    void returnTrueIfMatchesTestPlatform() {
        PlatformValidatorGuard guard = new PlatformValidatorGuard(TestPlatform.class);
        when(context.getPlatform()).thenReturn(new TestPlatform());

        StepVerifier.create(guard.matches(context))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void returnFalseIfDoesntMatchesTestPlatform() {
        PlatformValidatorGuard guard = new PlatformValidatorGuard(TestPlatform.class);
        when(context.getPlatform()).thenReturn(new NotTestPlatform());

        StepVerifier.create(guard.matches(context))
                .expectNext(false)
                .verifyComplete();
    }

    private class TestPlatform implements Platform {
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
            return "test-1";
        }
    }

    private class NotTestPlatform implements Platform {
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
            return "test-2";
        }
    }
}
package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.when;

class MapContainsKeyGuardTest extends BaseFracktailTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void returnTrueWhenKeyIsPresent() {
        MapContainsKeyGuard guard = new MapContainsKeyGuard("key");
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("key", "value")));

        StepVerifier.create(guard.matches(context))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void returnFalseWhenKeyIsNotPresent() {
        MapContainsKeyGuard guard = new MapContainsKeyGuard("key");
        when(context.getMap()).thenReturn(new AsynchronousMap<>());

        StepVerifier.create(guard.matches(context))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void returnFalseWhenMapIsNull() {
        MapContainsKeyGuard guard = new MapContainsKeyGuard("key");
        when(context.getMap()).thenReturn(null);

        StepVerifier.create(guard.matches(context))
                .expectNext(false)
                .verifyComplete();
    }
}
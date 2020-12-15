package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.modules.meta.CommandLookupContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class ParameterSizeGuardTest extends BaseFracktailTest {
    @BeforeEach
    public void parentSetup() {
        super.parentSetup();
    }

    @Test
    void returnTrueWhenParameterFallsWithinRange() {
        ParameterSizeGuard guard = new ParameterSizeGuard(0, 1);
        when(parameters.getNumberOfParameters()).thenReturn(0);

        StepVerifier.create(guard.matches(context))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void returnFalseWhenParameterFallsOutsideOfRange() {
        ParameterSizeGuard guard = new ParameterSizeGuard(0, 1);
        when(parameters.getNumberOfParameters()).thenReturn(2);

        StepVerifier.create(guard.matches(context))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void returnTrueWhenNonCommandUseContext() {
        ParameterSizeGuard guard = new ParameterSizeGuard(0, 1);
        CommandLookupContext<?> pbc = new CommandLookupContext<>(context);

        StepVerifier.create(guard.matches(context))
                .expectNext(true)
                .verifyComplete();
    }
}
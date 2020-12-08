package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ParameterRangeToStringHandlerTest extends BaseFracktailTest {
    private final ParameterRangeToStringHandler handler = new ParameterRangeToStringHandler();

    @BeforeEach
    void setUp() {
        this.parentSetup();
    }

    @Test
    void shouldReturnRawWhenInvoked() {
        when(parameters.getRaw()).thenReturn("hello, world");
        Object obj = handler.apply(context);

        assertEquals("hello, world", obj);
    }
}
package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class ParameterToObjectConverterFunctionTest extends BaseFracktailTest {
    @Mock
    private ParameterConverters converters;

    @BeforeEach
    private void setUp() {
        super.parentSetup();
    }

    @Test
    void returnParameterWhenPresentWithNoConversion() {
        when(parameters.getParameter(anyInt())).thenReturn(Optional.of("hello"));
        when(converters.convertToType(any(), any())).thenReturn("hello");
        ParameterToObjectConverterFunction handler = new ParameterToObjectConverterFunction(String.class, 0, converters);

        assertEquals("hello", handler.apply(context));
    }

    @Test
    void convertParameterWhenPresent() {
        when(parameters.getParameter(anyInt())).thenReturn(Optional.of("one"));
        when(converters.convertToType(any(), any())).thenReturn(1);
        ParameterToObjectConverterFunction handler = new ParameterToObjectConverterFunction(String.class, 0, converters);

        assertEquals(1, handler.apply(context));
    }

    @Test
    void returnDefaultWhenAbsent() {
        when(parameters.getParameter(anyInt())).thenReturn(Optional.empty());
        ParameterToObjectConverterFunction handler = new ParameterToObjectConverterFunction(String.class, 0, converters);

        assertNull(handler.apply(context));
    }
}
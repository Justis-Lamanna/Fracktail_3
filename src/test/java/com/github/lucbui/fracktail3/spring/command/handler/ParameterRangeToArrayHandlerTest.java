package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ParameterRangeToArrayHandlerTest extends BaseFracktailTest {
    @Mock
    private ParameterConverters converters;

    @BeforeEach
    void setUp() {
        super.parentSetup();
    }

    @Test
    void arrayIdentityShouldReturnCorrectly() {
        when(parameters.getParameters(eq(0), eq(2))).thenReturn(Arrays.asList(Optional.of("one"), Optional.of("two"), Optional.of("three")));
        when(converters.convertToType(any(), eq(String.class))).thenReturn("one", "two", "three");
        ParameterRangeToArrayHandler handler = new ParameterRangeToArrayHandler(0, 2, String.class, converters);

        Object obj = handler.apply(context);
        assertArrayEquals(new String[]{"one", "two", "three"}, (String[])obj);
    }

    @Test
    void arrayIdentityShouldReturnWithDefaultsIfNone() {
        when(parameters.getParameters(eq(0), eq(2))).thenReturn(Arrays.asList(Optional.of("one"), Optional.empty(), Optional.empty()));
        when(converters.convertToType(any(), eq(String.class))).thenReturn("one", "two", "three");
        ParameterRangeToArrayHandler handler = new ParameterRangeToArrayHandler(0, 2, String.class, converters);

        Object obj = handler.apply(context);
        assertArrayEquals(new String[]{"one", null, null}, (String[])obj);
    }

    @Test
    void arrayIdentityShouldConvertAndReturnCorrectly() {
        when(parameters.getParameters(eq(0), eq(2))).thenReturn(Arrays.asList(Optional.of("one"), Optional.of("two"), Optional.of("three")));
        when(converters.convertToType(any(), eq(Integer.TYPE))).thenReturn(1, 2, 3);
        ParameterRangeToArrayHandler handler = new ParameterRangeToArrayHandler(0, 2, Integer.TYPE, converters);

        Object obj = handler.apply(context);
        assertArrayEquals(new int[]{1, 2, 3}, (int[])obj);
    }

}
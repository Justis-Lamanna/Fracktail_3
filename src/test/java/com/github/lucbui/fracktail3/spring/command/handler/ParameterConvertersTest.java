package com.github.lucbui.fracktail3.spring.command.handler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParameterConvertersTest {
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private ParameterConverters converters;

    private AutoCloseable mocks;

    @BeforeEach
    private void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    private void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void nullShouldReturnDefault() {
        int value = converters.convertToType(null, Integer.class);
        assertEquals(0, value);
    }

    @Test
    void identityShouldReturnItself() {
        String value = converters.convertToType("hello", String.class);
        assertEquals("hello", value);
    }

    @Test
    void convertToOptionalShouldWrap() {
        Optional<?> opt = converters.convertToType("hello", Optional.class);
        assertTrue(opt.isPresent());
        assertEquals("hello", opt.get());
    }

    @Test
    void convertIfConvertersAllow() {
        when(conversionService.canConvert(eq(String.class), eq(Integer.class))).thenReturn(true);
        when(conversionService.convert(eq("one"), eq(Integer.class))).thenReturn(1);

        int value = converters.convertToType("one", Integer.class);
        assertEquals(1, value);

        verify(conversionService).convert(eq("one"), eq(Integer.class));
    }

    @Test
    void defaultIfEverythingElseFails() {
        assertThrows(ClassCastException.class, () -> {
            when(conversionService.canConvert(eq(String.class), eq(Integer.class))).thenReturn(false);
            int value = converters.convertToType("one", Integer.class);
        });
    }
}
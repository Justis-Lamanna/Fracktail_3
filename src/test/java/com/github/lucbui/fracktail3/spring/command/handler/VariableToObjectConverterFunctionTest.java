package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class VariableToObjectConverterFunctionTest extends BaseFracktailTest {
    @Mock
    private ParameterConverters converters;

    @BeforeEach
    void setUp() {
        this.parentSetup();
    }

    @Test
    void returnVariableWhenPresentWithNoConversion() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("greeting", "hello")));
        when(converters.convertToType(eq("hello"), eq(String.class))).thenReturn("hello");

        VariableToObjectConverterFunction function = new VariableToObjectConverterFunction(String.class, "greeting", converters);
        assertEquals("hello", function.apply(context));
    }

    @Test
    void returnVariableWhenPresentWithConversion() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("number", "one")));
        when(converters.convertToType(eq("one"), eq(Integer.TYPE))).thenReturn(1);

        VariableToObjectConverterFunction function = new VariableToObjectConverterFunction(Integer.TYPE, "number", converters);
        assertEquals(1, function.apply(context));
    }

    @Test
    void returnDefaultWhenAbsent() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>());

        VariableToObjectConverterFunction function = new VariableToObjectConverterFunction(Integer.TYPE, "number", converters);
        assertEquals(0, function.apply(context));
    }

    @Test
    void returnMonoWhenInputIsMono() {
        AsynchronousMap<String, Object> map = new AsynchronousMap<>();
        map.putAsync("greeting", Mono.just("hello"));
        when(context.getMap()).thenReturn(map);

        VariableToObjectConverterFunction function = new VariableToObjectConverterFunction(Mono.class, "greeting", converters);

        StepVerifier.create((Mono<String>)function.apply(context))
                .expectNext("hello")
                .verifyComplete();
    }
}
package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.annotation.Variable;
import com.github.lucbui.fracktail3.spring.command.handler.VariableToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.util.function.Tuple2;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class VariableStrategyTest {
    @Mock
    ParameterConverters converters;

    @InjectMocks
    VariableStrategy strategy;

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
    void methodWithoutAnnotationShouldReturnNothing() {
        Tuple2<Method, Parameter> tuple = TestUtils.getMethodAndParameter(getClass(), "methodWithoutAnnotation", "p");
        assertFalse(strategy.create(this, tuple.getT1(), tuple.getT2()).isPresent());
    }

    @Test
    void methodWithAnnotationShouldReturnSomething() {
        Tuple2<Method, Parameter> tuple = TestUtils.getMethodAndParameter(getClass(), "methodWithAnnotation", "p");
        assertTrue(strategy.create(this, tuple.getT1(), tuple.getT2()).isPresent());
    }

    @Test
    void methodWithAnnotationShouldReturnCorrectComponent() {
        Tuple2<Method, Parameter> tuple = TestUtils.getMethodAndParameter(getClass(), "methodWithAnnotation", "p");
        ParameterComponent component = strategy.create(this, tuple.getT1(), tuple.getT2()).orElseGet(Assertions::fail);

        assertEquals(VariableToObjectConverterFunction.class, component.getFunc().getClass());
    }

    // Test Methods
    public void methodWithoutAnnotation(String p) {}

    public void methodWithAnnotation(@Variable("greeting") String p) {}
}
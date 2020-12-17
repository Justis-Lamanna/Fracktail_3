package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.command.annotation.Payload;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.util.function.Tuple2;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayloadStrategyTest {
    @Mock
    ParameterConverters converters;

    @InjectMocks
    PayloadStrategy strategy;

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

    // Test Methods
    public void methodWithoutAnnotation(Object p) {}

    public void methodWithAnnotation(@Payload Object p) {}
}
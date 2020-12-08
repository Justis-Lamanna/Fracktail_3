package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.annotation.Parameter;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ParameterStrategyTest {
    @Mock
    private ParameterConverters converters;

    @InjectMocks
    private ParameterStrategy strategy;

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
    void testAnnotatedParameterReturns() {
        Tuple2<Method, java.lang.reflect.Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "parameterAnnotated", "p");
        ParameterComponent component = strategy.create(this, pair.getT1(), pair.getT2())
                .orElseGet(Assertions::fail);

        assertEquals(ParameterToObjectConverterFunction.class, component.getFunc().getClass());
    }

    @Test
    void testNoAnnotatedParameterReturnsEmpty() {
        Tuple2<Method, java.lang.reflect.Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "noParameterAnnotated", "p");
        assertFalse(strategy.create(this, pair.getT1(), pair.getT2()).isPresent());
    }

    //Test Methods
    public void parameterAnnotated(@Parameter(0) String p) {}

    public void noParameterAnnotated(String p) {}
}
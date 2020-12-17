package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToArrayConverterFunction;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToStringConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParameterRangeStrategyTest {
    @Mock
    private ParameterConverters converters;

    @InjectMocks
    private ParameterRangeStrategy strategy;

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
    void testAnnotatedWithBothWithArrayParameter() {
        Tuple2<Method, Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "parameterAnnotatedBoth", "p");
        ParameterComponent component = strategy.create(this, pair.getT1(), pair.getT2())
                .orElseGet(Assertions::fail);

        assertEquals(ParameterRangeToArrayConverterFunction.class, component.getFunc().getClass());
    }

    @Test
    void testAnnotatedWithDefaultsWithArrayParameter() {
        Tuple2<Method, Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "parameterAnnotatedNone", "p");
        ParameterComponent component = strategy.create(this, pair.getT1(), pair.getT2())
                .orElseGet(Assertions::fail);

        assertEquals(ParameterRangeToArrayConverterFunction.class, component.getFunc().getClass());
    }

    @Test
    void testAnnotatedWithStringParameter() {
        Tuple2<Method, Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "parameterAnnotatedString", "p");
        ParameterComponent component = strategy.create(this, pair.getT1(), pair.getT2())
                .orElseGet(Assertions::fail);

        assertEquals(ParameterRangeToStringConverterFunction.class, component.getFunc().getClass());
    }

    @Test
    void testAnnotatedWithNonDefaultsWithStringParameter() {
        Tuple2<Method, Parameter> pair = TestUtils.getMethodAndParameter(getClass(), "parameterAnnotatedStringNonDefaults", "p");
        assertThrows(BotConfigurationException.class, () -> {
            ParameterComponent component = strategy.create(this, pair.getT1(), pair.getT2())
                    .orElseGet(Assertions::fail);
        });
    }

    // Test Methods
    public void parameterAnnotatedBoth(@ParameterRange(upper = 4, lower = 1) String[] p) {}

    public void parameterAnnotatedNone(@ParameterRange String[] p) {}

    public void parameterAnnotatedString(@ParameterRange String p) {}

    public void parameterAnnotatedStringNonDefaults(@ParameterRange(upper = 4, lower = 1) String p) {}
}
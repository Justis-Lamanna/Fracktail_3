package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.schedule.annotation.InjectPlatform;
import com.github.lucbui.fracktail3.spring.schedule.handler.InjectPlatformHandler;
import org.junit.jupiter.api.Test;
import reactor.util.function.Tuple2;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InjectPlatformStrategyTest {
    private final InjectPlatformStrategy strategy = new InjectPlatformStrategy();

    @Test
    void shouldReturnNothingWhenNoAnnotation() {
        Tuple2<Method, Parameter> items = TestUtils.getMethodAndParameter(getClass(), "testNoAnnotation", "platform");

        Optional<ParameterComponent> component = strategy.create(this, items.getT1(), items.getT2());
        assertFalse(component.isPresent());
    }

    @Test
    void shouldReturnSomethingWhenAnnotation() {
        Tuple2<Method, Parameter> items = TestUtils.getMethodAndParameter(getClass(), "testWithAnnotation", "platform");

        Optional<ParameterComponent> component = strategy.create(this, items.getT1(), items.getT2());
        assertTrue(component.isPresent());
        assertTrue(component.get().getFunc() instanceof InjectPlatformHandler);
        assertNull(((InjectPlatformHandler) component.get().getFunc()).getId());
    }

    @Test
    void shouldReturnSomethingWhenAnnotationWithId() {
        Tuple2<Method, Parameter> items = TestUtils.getMethodAndParameter(getClass(), "testWithAnnotationTest", "platform");

        Optional<ParameterComponent> component = strategy.create(this, items.getT1(), items.getT2());
        assertTrue(component.isPresent());
        assertTrue(component.get().getFunc() instanceof InjectPlatformHandler);
        assertEquals("test", ((InjectPlatformHandler) component.get().getFunc()).getId());
    }

    // ---- Test ----
    public void testNoAnnotation(Platform platform) {}

    public void testWithAnnotation(@InjectPlatform Platform platform) {}

    public void testWithAnnotationTest(@InjectPlatform("test") Platform platform) {}

}
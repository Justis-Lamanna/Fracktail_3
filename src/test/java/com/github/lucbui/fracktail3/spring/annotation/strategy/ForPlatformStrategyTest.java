package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.TestPlatform;
import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.command.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.guard.PlatformValidatorGuard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForPlatformStrategyTest {
    private final ForPlatformStrategy strategy = new ForPlatformStrategy();

    @Test
    void forPlatformAnnotatedMethodShouldHaveGuard() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodOne"), new MethodComponent());
        PlatformValidatorGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), PlatformValidatorGuard.class);
        assertEquals(TestPlatform.class, guard.getPlatform());
    }

    @Test
    void forPlatformAnnotatedFieldShouldHaveGuard() {
        MethodComponent component = strategy.decorate(this, TestUtils.getField(getClass(),"fieldOne"), new MethodComponent());
        PlatformValidatorGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), PlatformValidatorGuard.class);
        assertEquals(TestPlatform.class, guard.getPlatform());
    }

    // -------- Test Methods ---------

    @Command
    @ForPlatform(TestPlatform.class)
    public String fieldOne = "";

    @Command
    @ForPlatform(TestPlatform.class)
    public void methodOne() {}
}
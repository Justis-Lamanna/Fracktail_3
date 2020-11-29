package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.TestPlatform;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.annotation.Parameter;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.guard.ParameterSizeGuard;
import com.github.lucbui.fracktail3.spring.command.guard.PlatformValidatorGuard;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MethodComponentFactoryTest {
    @Mock
    private ConversionService conversionService;

    @Spy
    private Plugins plugins = new Plugins();

    @InjectMocks
    private MethodComponentFactory factory;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    <T> T assertComponentHasGuard(MethodComponent component, Class<T> clazz) {
        return component.guards.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseGet(Assertions::fail);
    }

    @Test
    void forPlatformAnnotatedMethodShouldHaveGuard() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodOne"));
        PlatformValidatorGuard guard = assertComponentHasGuard(component, PlatformValidatorGuard.class);
        assertEquals(TestPlatform.class, guard.getPlatform());
    }

    @Test
    void forPlatformAnnotatedFieldShouldHaveGuard() {
        MethodComponent component = factory.compileField(this, getField("fieldOne"));
        PlatformValidatorGuard guard = assertComponentHasGuard(component, PlatformValidatorGuard.class);
        assertEquals(TestPlatform.class, guard.getPlatform());
    }

    @Test
    void methodWithParameterShouldEnforceGuard() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOneParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(1, guard.getMaximum());
    }

    @Test
    void methodWithTwoParametersShouldEnforceGuard() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithTwoParameters"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithTwoParametersButOneIgnoredShouldEnforceGuard() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithTwoParametersAndOneIgnored"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithOneOptionalParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOneOptionalParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(1, guard.getMaximum());
    }

    @Test
    void methodWithOneRequiredAndOneOptionalParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOneRequiredAndOneOptionalParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithOneOptionalAndOneRequiredParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOneOptionalAndOneRequiredParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithNegativeIndexParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNegativeTwoParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void methodWithNegativeIndexOptionalParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNegativeTwoOptionalParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void methodWithNegativeAndPositiveIndexParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNegativeIndexAndPositiveIndexParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithOptionalNegativeAndRequiredPositiveIndexParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNegativeOptionalIndexAndPositiveIndexParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithRequiredNegativeAndOptionalPositiveIndexParameter() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNegativeIndexAndPositiveOptionalIndexParameter"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithInvalidRangeShouldThrowException() {
        assertThrows(BotConfigurationException.class, () -> {
            MethodComponent component = factory.compileMethod(this, getMethod("methodWithIllegalRange"));
        });
    }

    @Test
    void testWithImplicitRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithImplicitRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithImplicitLowerRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithImplicitLowerRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithImplicitUpperRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithImplicitUpperRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithExplicitRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithUpperAndLowerRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithOptionalRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOptionalRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithOptionalNegativeRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOptionalNegativeRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithMixedRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithRangeAndNonRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithMixedRangeOneRequiredOneOptional() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithRangeAndOptionalNonRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithMixedRangeOneOptionalOneRequired() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOptionalRangeAndRequiredNonRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRanges() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOverlappingRanges"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRangesNonOverlapping() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithNonOverlappingRanges"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(9, guard.getMinimum());
        assertEquals(9, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRangesOverlappingOneOptional() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithOverlappingRangesOneOptional"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithOverlappingRangeAndNonRange() {
        MethodComponent component = factory.compileMethod(this, getMethod("methodWithRangeAndOverlappingNonRange"));
        ParameterSizeGuard guard = assertComponentHasGuard(component, ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    //------------------------------Test Methods------------------------------------------------------------------------
    private Method getMethod(String name) {
        return Arrays.stream(getClass().getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private Field getField(String name) {
        return Arrays.stream(getClass().getFields())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @ForPlatform(TestPlatform.class)
    public String fieldOne = "";

    @ForPlatform(TestPlatform.class)
    public void methodOne() {}

    public void methodWithOneParameter(@Parameter(0) String p1) {}

    public void methodWithTwoParameters(@Parameter(0) String p1, @Parameter(1) String p2) {}

    public void methodWithTwoParametersAndOneIgnored(@Parameter(1) String p2) {}

    public void methodWithOneOptionalParameter(@Parameter(value = 0, optional = true) String p1) {}

    public void methodWithOneRequiredAndOneOptionalParameter(@Parameter(0) String p1, @Parameter(value = 1, optional = true) String p2) {}

    public void methodWithOneOptionalAndOneRequiredParameter(@Parameter(value = 0, optional = true) String p1, @Parameter(1) String p2) {}

    public void methodWithNegativeTwoParameter(@Parameter(-1) String last) {}

    public void methodWithNegativeTwoOptionalParameter(@Parameter(value = -1, optional = true) String lastOpt) {}

    public void methodWithNegativeIndexAndPositiveIndexParameter(@Parameter(-1) String last, @Parameter(2) String second) {}

    public void methodWithNegativeOptionalIndexAndPositiveIndexParameter(@Parameter(value = -1, optional = true) String last, @Parameter(2) String second) {}

    public void methodWithNegativeIndexAndPositiveOptionalIndexParameter(@Parameter(-1) String last, @Parameter(value = 2, optional = true) String second) {}

    public void methodWithIllegalRange(@ParameterRange(lower = 3, upper = 1) String[] params) {}

    public void methodWithImplicitRange(@ParameterRange String[] params) {}

    public void methodWithImplicitLowerRange(@ParameterRange(upper = 4) String[] params) {}

    public void methodWithImplicitUpperRange(@ParameterRange(lower = 2) String[] params) {}

    public void methodWithUpperAndLowerRange(@ParameterRange(lower = 2, upper = 4) String[] params) {}

    public void methodWithOptionalRange(@ParameterRange(upper = 4, optional = true) String[] params) {}

    public void methodWithOptionalNegativeRange(@ParameterRange(upper = -2, optional = true) String[] params) {}

    public void methodWithRangeAndNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(6) String sixth) {}

    public void methodWithRangeAndOptionalNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(value = 6, optional = true) String sixth) {}

    public void methodWithOptionalRangeAndRequiredNonRange(@ParameterRange(lower = 0, upper = 4, optional = true) String[] params, @Parameter(6) String sixth) {}

    public void methodWithOverlappingRanges(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 2, upper = 6) String another) {}

    public void methodWithNonOverlappingRanges(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 5, upper = 8) String another) {}

    public void methodWithOverlappingRangesOneOptional(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 2, upper = 6, optional = true) String another) {}

    public void methodWithRangeAndOverlappingNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(2) String second) {}
}
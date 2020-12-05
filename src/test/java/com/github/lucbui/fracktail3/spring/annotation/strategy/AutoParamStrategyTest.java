package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.Parameter;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.guard.ParameterSizeGuard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AutoParamStrategyTest {
    private final AutoParamStrategy strategy = new AutoParamStrategy();

    @Test
    void methodWithParameterShouldEnforceGuard() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOneParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(1, guard.getMaximum());
    }

    @Test
    void methodWithTwoParametersShouldEnforceGuard() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithTwoParameters"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithTwoParametersButOneIgnoredShouldEnforceGuard() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithTwoParametersAndOneIgnored"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithOneOptionalParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOneOptionalParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(1, guard.getMaximum());
    }

    @Test
    void methodWithOneRequiredAndOneOptionalParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOneRequiredAndOneOptionalParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithOneOptionalAndOneRequiredParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOneOptionalAndOneRequiredParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(2, guard.getMinimum());
        assertEquals(2, guard.getMaximum());
    }

    @Test
    void methodWithNegativeIndexParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNegativeTwoParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void methodWithNegativeIndexOptionalParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNegativeTwoOptionalParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void methodWithNegativeAndPositiveIndexParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNegativeIndexAndPositiveIndexParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithOptionalNegativeAndRequiredPositiveIndexParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNegativeOptionalIndexAndPositiveIndexParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithRequiredNegativeAndOptionalPositiveIndexParameter() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNegativeIndexAndPositiveOptionalIndexParameter"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(1, guard.getMinimum());
        assertEquals(3, guard.getMaximum());
    }

    @Test
    void methodWithInvalidRangeShouldThrowException() {
        assertThrows(BotConfigurationException.class, () -> {
            MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithIllegalRange"), new MethodComponent());
        });
    }

    @Test
    void testWithImplicitRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithImplicitRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithImplicitLowerRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithImplicitLowerRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithImplicitUpperRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithImplicitUpperRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(3, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithExplicitRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithUpperAndLowerRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithOptionalRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOptionalRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    @Test
    void testWithOptionalNegativeRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOptionalNegativeRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(0, guard.getMinimum());
        assertEquals(Integer.MAX_VALUE, guard.getMaximum());
    }

    @Test
    void testWithMixedRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithRangeAndNonRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithMixedRangeOneRequiredOneOptional() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithRangeAndOptionalNonRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithMixedRangeOneOptionalOneRequired() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOptionalRangeAndRequiredNonRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRanges() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOverlappingRanges"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(7, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRangesNonOverlapping() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithNonOverlappingRanges"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(9, guard.getMinimum());
        assertEquals(9, guard.getMaximum());
    }

    @Test
    void testWithTwoMixedRangesOverlappingOneOptional() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithOverlappingRangesOneOptional"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(7, guard.getMaximum());
    }

    @Test
    void testWithOverlappingRangeAndNonRange() {
        MethodComponent component = strategy.decorate(this, TestUtils.getMethod(getClass(), "methodWithRangeAndOverlappingNonRange"), new MethodComponent());
        ParameterSizeGuard guard = TestUtils.assertComponentHasGuard(component.getGuards(), ParameterSizeGuard.class);
        assertEquals(5, guard.getMinimum());
        assertEquals(5, guard.getMaximum());
    }

    //------------------------------Test Methods------------------------------------------------------------------------

    @Command
    public void methodWithOneParameter(@Parameter(0) String p1) {}

    @Command
    public void methodWithTwoParameters(@Parameter(0) String p1, @Parameter(1) String p2) {}

    @Command
    public void methodWithTwoParametersAndOneIgnored(@Parameter(1) String p2) {}

    @Command
    public void methodWithOneOptionalParameter(@Parameter(value = 0, optional = true) String p1) {}

    @Command
    public void methodWithOneRequiredAndOneOptionalParameter(@Parameter(0) String p1, @Parameter(value = 1, optional = true) String p2) {}

    @Command
    public void methodWithOneOptionalAndOneRequiredParameter(@Parameter(value = 0, optional = true) String p1, @Parameter(1) String p2) {}

    @Command
    public void methodWithNegativeTwoParameter(@Parameter(-1) String last) {}

    @Command
    public void methodWithNegativeTwoOptionalParameter(@Parameter(value = -1, optional = true) String lastOpt) {}

    @Command
    public void methodWithNegativeIndexAndPositiveIndexParameter(@Parameter(-1) String last, @Parameter(2) String second) {}

    @Command
    public void methodWithNegativeOptionalIndexAndPositiveIndexParameter(@Parameter(value = -1, optional = true) String last, @Parameter(2) String second) {}

    @Command
    public void methodWithNegativeIndexAndPositiveOptionalIndexParameter(@Parameter(-1) String last, @Parameter(value = 2, optional = true) String second) {}

    @Command
    public void methodWithIllegalRange(@ParameterRange(lower = 3, upper = 1) String[] params) {}

    @Command
    public void methodWithImplicitRange(@ParameterRange String[] params) {}

    @Command
    public void methodWithImplicitLowerRange(@ParameterRange(upper = 4) String[] params) {}

    @Command
    public void methodWithImplicitUpperRange(@ParameterRange(lower = 2) String[] params) {}

    @Command
    public void methodWithUpperAndLowerRange(@ParameterRange(lower = 2, upper = 4) String[] params) {}

    @Command
    public void methodWithOptionalRange(@ParameterRange(upper = 4, optional = true) String[] params) {}

    @Command
    public void methodWithOptionalNegativeRange(@ParameterRange(upper = -2, optional = true) String[] params) {}

    @Command
    public void methodWithRangeAndNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(6) String sixth) {}

    @Command
    public void methodWithRangeAndOptionalNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(value = 6, optional = true) String sixth) {}

    @Command
    public void methodWithOptionalRangeAndRequiredNonRange(@ParameterRange(lower = 0, upper = 4, optional = true) String[] params, @Parameter(6) String sixth) {}

    @Command
    public void methodWithOverlappingRanges(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 2, upper = 6) String another) {}

    @Command
    public void methodWithNonOverlappingRanges(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 5, upper = 8) String another) {}

    @Command
    public void methodWithOverlappingRangesOneOptional(@ParameterRange(lower = 0, upper = 4) String[] params, @ParameterRange(lower = 2, upper = 6, optional = true) String another) {}

    @Command
    public void methodWithRangeAndOverlappingNonRange(@ParameterRange(lower = 0, upper = 4) String[] params, @Parameter(2) String second) {}
}
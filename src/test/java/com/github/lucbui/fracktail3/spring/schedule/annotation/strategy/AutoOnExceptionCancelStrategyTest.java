package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.schedule.exception.CancelTaskException;
import com.github.lucbui.fracktail3.spring.schedule.handler.ExceptionCancelHandler;
import com.github.lucbui.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutoOnExceptionCancelStrategyTest {
    @Test
    void addsHandlerForMethod() {
        ExceptionScheduledComponent obj = new ExceptionScheduledComponent();
        AutoOnExceptionCancelStrategy strategy = new AutoOnExceptionCancelStrategy();
        strategy.decorateSchedule(new Object(), TestUtils.getAnyMethod(), obj);

        assertEquals(1, obj.getCandidates().size());
        assertTrue(obj.getCandidates().get(CancelTaskException.class) instanceof ExceptionCancelHandler);
    }

    @Test
    void addsHandlerForField() {
        ExceptionScheduledComponent obj = new ExceptionScheduledComponent();
        AutoOnExceptionCancelStrategy strategy = new AutoOnExceptionCancelStrategy();
        strategy.decorateSchedule(new Object(), TestUtils.getAnyField(), obj);

        assertEquals(1, obj.getCandidates().size());
        assertTrue(obj.getCandidates().get(CancelTaskException.class) instanceof ExceptionCancelHandler);
    }
}
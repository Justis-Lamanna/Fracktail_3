package com.github.lucbui.fracktail3.spring.annotation.schedule.strategy;

import com.github.lucbui.fracktail3.spring.command.handler.schedule.ExceptionCancelHandler;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionScheduledComponent;
import com.github.lucbui.fracktail3.spring.exception.CancelTaskException;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ExceptionScheduledComponentStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A strategy that, when CancelTaskException is thrown, cancels future runs of the scheduled action
 */
public class AutoOnExceptionCancelStrategy implements ExceptionScheduledComponentStrategy {
    @Override
    public ExceptionScheduledComponent decorateSchedule(Object obj, Method method, ExceptionScheduledComponent base) {
        base.addHandler(CancelTaskException.class, new ExceptionCancelHandler());
        return base;
    }

    @Override
    public ExceptionScheduledComponent decorateSchedule(Object obj, Field field, ExceptionScheduledComponent base) {
        base.addHandler(CancelTaskException.class, new ExceptionCancelHandler());
        return base;
    }
}

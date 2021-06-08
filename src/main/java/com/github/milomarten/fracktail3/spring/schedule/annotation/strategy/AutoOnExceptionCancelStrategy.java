package com.github.milomarten.fracktail3.spring.schedule.annotation.strategy;

import com.github.milomarten.fracktail3.spring.schedule.exception.CancelTaskException;
import com.github.milomarten.fracktail3.spring.schedule.handler.ExceptionCancelHandler;
import com.github.milomarten.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import com.github.milomarten.fracktail3.spring.schedule.plugin.ExceptionScheduledComponentStrategy;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A strategy that, when CancelTaskException is thrown, cancels future runs of the scheduled action
 */
@Order(0)
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

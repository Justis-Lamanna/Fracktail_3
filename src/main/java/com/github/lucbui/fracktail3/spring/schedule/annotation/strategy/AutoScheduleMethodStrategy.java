package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import com.github.lucbui.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.MethodScheduledComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoScheduleMethodStrategy implements MethodScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoScheduleMethodStrategy.class);

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base) {
        if(method.isAnnotationPresent(Schedule.class)) {
            base.setId(method.getAnnotation(Schedule.class).value());
        } else {
            base.setId(method.getName());
        }
        LOGGER.debug("Adding @Schedule-annotated method {}", base.getId());
        return base;
    }

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base) {
        if(field.isAnnotationPresent(Schedule.class)) {
            base.setId(field.getAnnotation(Schedule.class).value());
        } else {
            base.setId(field.getName());
        }
        LOGGER.debug("Adding @Schedule-annotated method {}", base.getId());
        return base;
    }
}

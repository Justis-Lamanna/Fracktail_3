package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ReturnScheduledComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ReturnConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class AutoScheduleReturnStrategy implements ReturnScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoScheduleReturnStrategy.class);

    @Autowired
    private ReturnConverters converters;

    @Override
    public Optional<ReturnScheduledComponent> createSchedule(Object obj, Method method) {
        return converters.getHandlerForType(method.getReturnType())
                .map(ReturnScheduledComponent::new);
    }

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Method method, ReturnScheduledComponent base) {
        return base;
    }

    @Override
    public Optional<ReturnScheduledComponent> createSchedule(Object obj, Field field) {
        return converters.getHandlerForType(field.getType())
                .map(ReturnScheduledComponent::new);
    }

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Field field, ReturnScheduledComponent base) {
        return base;
    }
}
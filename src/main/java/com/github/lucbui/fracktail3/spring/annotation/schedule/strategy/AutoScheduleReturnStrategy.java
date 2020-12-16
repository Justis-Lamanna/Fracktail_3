package com.github.lucbui.fracktail3.spring.annotation.schedule.strategy;

import com.github.lucbui.fracktail3.spring.command.model.ReturnScheduledComponent;
import com.github.lucbui.fracktail3.spring.command.service.ReturnConverters;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ReturnScheduledComponentStrategy;
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
        return converters.getScheduleHandlerForType(method.getReturnType())
                .map(ReturnScheduledComponent::new);
    }

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Method method, ReturnScheduledComponent base) {
        return base;
    }

    @Override
    public Optional<ReturnScheduledComponent> createSchedule(Object obj, Field field) {
        return converters.getScheduleHandlerForType(field.getType())
                .map(ReturnScheduledComponent::new);
    }

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Field field, ReturnScheduledComponent base) {
        return base;
    }
}

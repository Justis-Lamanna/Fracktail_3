package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ReturnScheduledComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ReturnConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class AutoScheduleReturnStrategy implements ReturnScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoScheduleReturnStrategy.class);

    @Autowired
    private ReturnConverters converters;

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Method method, ReturnScheduledComponent base) {
        base.setFunc(converters.getScheduleHandlerForType(method.getReturnType())
                .orElseThrow(() -> new BotConfigurationException("Unable to compile return on method " + method.getName())));
        return base;
    }

    @Override
    public ReturnScheduledComponent decorateSchedule(Object obj, Field field, ReturnScheduledComponent base) {
        base.setFunc(converters.getScheduleHandlerForType(field.getType())
                .orElseThrow(() -> new BotConfigurationException("Unable to compile return on field " + field.getName())));
        return base;
    }
}

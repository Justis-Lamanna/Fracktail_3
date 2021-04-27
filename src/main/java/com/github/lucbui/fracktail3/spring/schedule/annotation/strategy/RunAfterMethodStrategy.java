package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteAfterDurationTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAfter;
import com.github.lucbui.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.MethodScheduledComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class RunAfterMethodStrategy implements MethodScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunAfterMethodStrategy.class);

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base) {
        base.setTrigger(fromRunAfter(method.getAnnotation(RunAfter.class)));
        LOGGER.info("+-Set to RunAfter: {}", base.getTrigger());
        return base;
    }

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base) {
        base.setTrigger(fromRunAfter(field.getAnnotation(RunAfter.class)));
        LOGGER.info("+-Set to RunAfter: {}", base.getTrigger());
        return base;
    }

    protected static ScheduleEventTrigger fromRunAfter(RunAfter run) {
        try {
            Duration duration = Duration.parse(run.value());
            return new ExecuteAfterDurationTrigger(duration);
        } catch (DateTimeParseException ex) {
            throw new BotConfigurationException("Invalid duration " + run.value(), ex);
        }
    }
}

package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteOnInstantTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAt;
import com.github.lucbui.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.MethodScheduledComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RunAtMethodStrategy implements MethodScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunAtMethodStrategy.class);

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base) {
        base.setTrigger(fromRunAt(method.getAnnotation(RunAt.class)));
        LOGGER.info("+-Set to RunAt: {}", base.getTrigger());
        return base;
    }

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base) {
        base.setTrigger(fromRunAt(field.getAnnotation(RunAt.class)));
        LOGGER.info("+-Set to RunAt: {}", base.getTrigger());
        return base;
    }

    protected static ScheduleEventTrigger fromRunAt(RunAt run) {
        Instant instant;
        try {
            instant = ZonedDateTime.parse(run.value(), DateTimeFormatter.ISO_DATE_TIME).toInstant();
        } catch(DateTimeParseException ex) {
            try {
                instant = LocalDateTime.parse(run.value(), DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException ex2) {
                throw new BotConfigurationException("Invalid time " + run.value(), ex2);
            }
        }
        return new ExecuteOnInstantTrigger(instant);
    }
}

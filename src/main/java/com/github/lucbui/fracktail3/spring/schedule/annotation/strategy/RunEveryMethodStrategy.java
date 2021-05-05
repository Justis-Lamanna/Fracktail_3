package com.github.lucbui.fracktail3.spring.schedule.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteRepeatedlyTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunEvery;
import com.github.lucbui.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.MethodScheduledComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.format.DateTimeParseException;

@Order(0)
public class RunEveryMethodStrategy implements MethodScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunEveryMethodStrategy.class);

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base) {
        base.setTrigger(fromRunEvery(method.getAnnotation(RunEvery.class)));
        LOGGER.info("+-Set to RunEvery: {}", base.getTrigger());
        return base;
    }

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base) {
        base.setTrigger(fromRunEvery(field.getAnnotation(RunEvery.class)));
        LOGGER.info("+-Set to RunEvery: {}", base.getTrigger());
        return base;
    }

    protected static ScheduleEventTrigger fromRunEvery(RunEvery run) {
        try {
            Duration duration = Duration.parse(run.value());
            return new ExecuteRepeatedlyTrigger(duration);
        } catch (DateTimeParseException ex) {
            throw new BotConfigurationException("Invalid duration " + run.value(), ex);
        }
    }
}

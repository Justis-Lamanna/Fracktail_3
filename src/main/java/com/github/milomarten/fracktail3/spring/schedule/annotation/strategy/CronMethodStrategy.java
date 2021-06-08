package com.github.milomarten.fracktail3.spring.schedule.annotation.strategy;

import com.github.milomarten.fracktail3.magic.schedule.trigger.CronTrigger;
import com.github.milomarten.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.milomarten.fracktail3.spring.schedule.annotation.Cron;
import com.github.milomarten.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.milomarten.fracktail3.spring.schedule.plugin.MethodScheduledComponentStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;
import java.util.TimeZone;

@Order(0)
public class CronMethodStrategy implements MethodScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(CronMethodStrategy.class);

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base) {
        base.setTrigger(fromCron(method.getAnnotation(Cron.class)));
        LOGGER.info("+-Set to Cron: {}", base.getTrigger());
        return base;
    }

    @Override
    public MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base) {
        base.setTrigger(fromCron(field.getAnnotation(Cron.class)));
        LOGGER.info("+-Set to Cron string {}", base.getTrigger());
        return base;
    }

    protected static ScheduleEventTrigger fromCron(Cron cron) {
        StringJoiner sj = new StringJoiner(" ");
        String cronStr = sj
                .add(cron.second())
                .add(cron.minute())
                .add(cron.hour())
                .add(cron.dayOfMonth())
                .add(cron.month())
                .add(cron.dayOfWeek())
                .toString();
        if(StringUtils.isBlank(cron.timezone())) {
            return new CronTrigger(cronStr);
        } else {
            TimeZone timeZone = TimeZone.getTimeZone(cron.timezone());
            return new CronTrigger(cronStr, timeZone);
        }
    }
}

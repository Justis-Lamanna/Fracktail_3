package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.schedule.trigger.*;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAfter;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAt;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunEvery;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.StringJoiner;
import java.util.TimeZone;

/**
 * Utilities to let me parse annotation things
 */
public class AnnotationUtils {
    /**
     * Convert a @Cron annotation into a ScheduleEventTrigger
     * @param cron The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromCron(Cron cron) {
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

    /**
     * Convert a @RunAt annotation into a ScheduledEventTrigger
     * @param run The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromRunAt(RunAt run) {
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

    /**
     * Convert a @RunAfter annotation into a ScheduledEventTrigger
     * @param run The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromRunAfter(RunAfter run) {
        try {
            Duration duration = Duration.parse(run.value());
            return new ExecuteAfterDurationTrigger(duration);
        } catch (DateTimeParseException ex) {
            throw new BotConfigurationException("Invalid duration " + run.value(), ex);
        }
    }

    /**
     * Convert a @RunEvery annotation into a ScheduledEventTrigger
     * @param run The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromRunEvery(RunEvery run) {
        try {
            Duration duration = Duration.parse(run.value());
            return new ExecuteRepeatedlyTrigger(duration);
        } catch (DateTimeParseException ex) {
            throw new BotConfigurationException("Invalid duration " + run.value(), ex);
        }
    }
}

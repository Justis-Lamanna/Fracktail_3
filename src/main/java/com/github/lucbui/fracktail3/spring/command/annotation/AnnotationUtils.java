package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.schedule.trigger.*;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAfter;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAt;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunEvery;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Utilities to let me parse annotation things
 */
public class AnnotationUtils {
    /**
     * Reflectively create a FormattedString from an @FString annotation
     * @param fString The FString to use
     * @return The constucted FormattedString
     */
    public static FormattedString fromFString(FString fString) {
        return parse(fString::value, fString::formatters);
    }

    /**
     * Reflectively create a FormattedString from an @Usage annotation
     * @param usage The usage string to use
     * @return The constucted FormattedString
     */
    public static FormattedString fromUsage(Usage usage) {
        return parse(usage::value, usage::formatters);
    }

    /**
     * Reflectively create a FormattedString from suppliers
     * @param baseSup Supplier that creates the base string
     * @param formatSup Supplier that returns the list of Formatter objects
     * @return The constucted FormattedString
     */
    public static FormattedString parse(Supplier<String> baseSup, Supplier<Formatter[]> formatSup) {
        String base = baseSup.get();
        Formatter[] formatters = formatSup.get();
        if(formatters.length == 0) {
            return FormattedString.from(base);
        } else {
            return FormattedString.from(base, fromFormatterArray(formatters));
        }
    }

    private static ContextFormatter fromFormatterArray(Formatter[] formatters) {
        return Arrays.stream(formatters)
                .map(AnnotationUtils::fromFormatter)
                .reduce(ContextFormatter.identity(), ContextFormatter::pipe);
    }

    private static ContextFormatter fromFormatter(Formatter formatter) {
        Class<? extends ContextFormatter> clazz = formatter.value();
        Class<?>[] constructorSignature = IntStream.range(0, formatter.params().length)
                .mapToObj(x -> String.class)
                .toArray(Class[]::new);
        try {
            Constructor<? extends ContextFormatter> constructor = clazz.getConstructor(constructorSignature);
            return constructor.newInstance((Object[]) formatter.params());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new BotConfigurationException("Cannot invoke constructor in class " + clazz.getCanonicalName() + " with " + constructorSignature.length + " string args", ex);
        }
    }

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
        Instant instant = ZonedDateTime.parse(run.value(), DateTimeFormatter.ISO_DATE_TIME).toInstant();
        return new ExecuteOnInstantTrigger(instant);
    }

    /**
     * Convert a @RunAfter annotation into a ScheduledEventTrigger
     * @param run The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromRunAfter(RunAfter run) {
        Duration duration = Duration.parse(run.value());
        return new ExecuteAfterDurationTrigger(duration);
    }

    /**
     * Convert a @RunEvery annotation into a ScheduledEventTrigger
     * @param run The annotation
     * @return The created trigger
     */
    public static ScheduleEventTrigger fromRunEvery(RunEvery run) {
        Duration duration = Duration.parse(run.value());
        return new ExecuteRepeatedlyTrigger(duration);
    }
}

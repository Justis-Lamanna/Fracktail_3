package com.github.milomarten.fracktail3.spring.schedule.annotation;

import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.RunAtMethodStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.MethodScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the annotated action should run at an exact instant.
 *
 * Instant format is the standard ISO format, with or without offset
 * (YYYY-MM-DDTHH:MM:SS[timezone])
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodScheduleStrategy(RunAtMethodStrategy.class)
public @interface RunAt {
    /**
     * The time to run, according to ISO_DATE_TIME formatting
     * @return The time to run
     * @see java.time.format.DateTimeFormatter#ISO_DATE_TIME
     */
    String value();
}

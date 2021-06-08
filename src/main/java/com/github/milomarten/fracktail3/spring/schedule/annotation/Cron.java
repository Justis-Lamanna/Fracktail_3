package com.github.milomarten.fracktail3.spring.schedule.annotation;

import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.CronMethodStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.MethodScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the Schedule should run according to a Cron expression
 * Rather than using a full expression, we break it up into parts to make remembering easier.
 * Potential to-do: annotation which allows a complete expression
 *
 * If no fields are specified, the generated Cron expression runs at midnight every day.
 *
 * Exercise caution with day-of-month and day-of-week: Typically, if one is specified, the other should be "?" (any).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodScheduleStrategy(CronMethodStrategy.class)
public @interface Cron {
    /**
     * The cron "second" expression. Default is 0.
     * @return The second expression
     */
    String second() default "0";

    /**
     * The cron "minute" expression. Default is 0.
     * @return The minute expression
     */
    String minute() default "0";

    /**
     * The cron "hour" expression. Default is 0.
     * @return The hour expression
     */
    String hour() default "0";

    /**
     * The cron "day of month" expression. Default is * (all)
     * @return The day of month expression
     */
    String dayOfMonth() default "*";

    /**
     * The cron "month" expression. Default is * (all)
     * @return The month expression
     */
    String month() default "*";

    /**
     * The cron "day of week" expresion. Default is ? (any)
     * @return The day of week expression
     */
    String dayOfWeek() default "?";

    /**
     * The timezone to evaluate the expression in.
     * Supports abbreviations, full (tz) names, or a UTC offset
     * @return
     */
    String timezone() default "";
}

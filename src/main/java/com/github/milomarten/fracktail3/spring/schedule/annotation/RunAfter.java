package com.github.milomarten.fracktail3.spring.schedule.annotation;

import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.RunAfterMethodStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.MethodScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the annotated action should run after waiting the provided duration after startup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodScheduleStrategy(RunAfterMethodStrategy.class)
public @interface RunAfter {
    /**
     * The duration to wait, in ISO format
     * @return The duration to wait
     * @see java.time.Duration#parse(CharSequence)
     */
    String value();
}

package com.github.lucbui.fracktail3.spring.schedule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify the annotated action should run repeated, waiting the provided duration between runs
 *
 * Note that this runs immediately at startup
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface RunEvery {
    /**
     * The duration to wait, in ISO format
     * @return The duration to wait
     * @see java.time.Duration#parse(CharSequence)
     */
    String value();
}

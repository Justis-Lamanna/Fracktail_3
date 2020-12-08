package com.github.lucbui.fracktail3.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies usage clues on how to use this command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Usage {
    /**
     * The raw string value
     * @return raw string value
     */
    String value();

    /**
     * One or more formatters to transform the raw value
     * @return The formatters to use
     */
    Formatter[] formatters() default {};
}

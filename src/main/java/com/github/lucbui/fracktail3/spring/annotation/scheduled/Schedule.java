package com.github.lucbui.fracktail3.spring.annotation.scheduled;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method or field as a scheduled action
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Schedule {
    /**
     * The ID of the scheduled action. If empty, uses the method/field name
     * @return Scheduled action ID
     */
    String value() default "";
}

package com.github.lucbui.fracktail3.spring.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify one or more names for a command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Name {
    /**
     * One or more names for this command
     * @return The command names
     */
    String[] value() default {};
}

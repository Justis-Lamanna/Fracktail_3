package com.github.milomarten.fracktail3.spring.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies usage clues on how to use this command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface Usage {
    /**
     * The raw string value
     * @return raw string value
     */
    String value();
}

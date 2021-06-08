package com.github.milomarten.fracktail3.spring.command.annotation;

import com.github.milomarten.fracktail3.spring.command.annotation.strategy.GuardedStrategy;
import com.github.milomarten.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows specification of an arbitrary SpEL expression as a guard.
 * The command use context is exposed completely, allowing for any field in it to be queried.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(GuardedStrategy.class)
public @interface Guarded {
    /**
     * SpEL expression returning true or false
     * @return SpEL expression
     */
    String value();
}

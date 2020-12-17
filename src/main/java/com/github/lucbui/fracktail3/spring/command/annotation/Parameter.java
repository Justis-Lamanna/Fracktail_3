package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter to be injected with a command usage parameter at the specified index
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@com.github.lucbui.fracktail3.spring.command.plugin.ParameterStrategy(ParameterStrategy.class)
public @interface Parameter {
    /**
     * The index of the command parameter to inject.
     * Negative indexing is supported, so -1 returns the last parameter, -2 second-to-last, and so on.
     * @return The index of the command parameter to inject
     */
    int value();
    @Deprecated
    boolean optional() default false;
}

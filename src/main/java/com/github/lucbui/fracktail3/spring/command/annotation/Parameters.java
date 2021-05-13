package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.ParametersStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter to be injected with a command usage parameter at the specified index
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@MethodStrategy(ParametersStrategy.class)
public @interface Parameters {
    /**
     * A list of additional parameters to add to this method or field
     */
    Parameter[] value();
}

package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.VariableStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a parameter to be injected with the value of a command usage variable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(VariableStrategy.class)
public @interface Variable {
    /**
     * The key of the variable to inject
     * @return The key of the variable to inject
     */
    String value();
    @Deprecated
    boolean optional() default false;
}

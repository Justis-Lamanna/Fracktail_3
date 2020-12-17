package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.ParameterRangeStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a range of parameters to be injected with a command usage parameter at the specified range
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(ParameterRangeStrategy.class)
public @interface ParameterRange {
    /**
     * The lower bound of the range.
     * By default, lower bound is 0.
     * @return The lower bound of the range.
     */
    int lower() default 0;

    /**
     * The upper bound of the range.
     * By default, lower bound is -1 (last parameter).
     * Negative indexing is supported, so -1 returns up to the last parameter, -2 up to the second-to-last, and so on.
     * @return
     */
    int upper() default -1;
    @Deprecated
    boolean optional() default false;
}

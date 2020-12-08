package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.ParameterRangeStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(ParameterRangeStrategy.class)
public @interface ParameterRange {
    int lower() default 0;
    int upper() default -1;
    @Deprecated
    boolean optional() default false;
}

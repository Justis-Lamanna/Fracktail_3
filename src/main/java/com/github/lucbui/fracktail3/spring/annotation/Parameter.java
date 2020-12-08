package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@com.github.lucbui.fracktail3.spring.plugin.v2.ParameterStrategy(ParameterStrategy.class)
public @interface Parameter {
    int value();
    @Deprecated
    boolean optional() default false;
}

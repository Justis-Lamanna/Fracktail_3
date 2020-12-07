package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.ParameterAnnotationStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(ParameterAnnotationStrategy.class)
public @interface Parameter {
    int value();
    boolean optional() default false;
}

package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.AutoReturnStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ReturnStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@ReturnStrategy(AutoReturnStrategy.class)
public @interface Command {
    String value() default "";
}

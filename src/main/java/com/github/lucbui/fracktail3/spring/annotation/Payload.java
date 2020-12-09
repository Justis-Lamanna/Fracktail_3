package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.spring.annotation.strategy.PayloadStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(PayloadStrategy.class)
public @interface Payload {
}

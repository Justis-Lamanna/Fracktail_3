package com.github.milomarten.fracktail3.spring.command.annotation;

import com.github.milomarten.fracktail3.spring.command.annotation.strategy.InjectPlaceStrategy;
import com.github.milomarten.fracktail3.spring.command.plugin.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(InjectPlaceStrategy.class)
public @interface InjectPlace {
    String platform() default "";
    String id() default "";
}

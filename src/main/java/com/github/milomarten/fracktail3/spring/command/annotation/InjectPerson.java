package com.github.milomarten.fracktail3.spring.command.annotation;

import com.github.milomarten.fracktail3.spring.command.annotation.strategy.InjectPersonStrategy;
import com.github.milomarten.fracktail3.spring.command.plugin.ParameterStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterStrategy(InjectPersonStrategy.class)
public @interface InjectPerson {
    String platform() default "";
    String id() default "";
}

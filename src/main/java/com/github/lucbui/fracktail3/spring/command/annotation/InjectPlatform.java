package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.InjectPlatformStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterStrategy;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ParameterScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterScheduleStrategy(InjectPlatformStrategy.class)
@ParameterStrategy(InjectPlatformStrategy.class)
public @interface InjectPlatform {
    String value() default "";
}

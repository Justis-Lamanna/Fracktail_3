package com.github.lucbui.fracktail3.spring.annotation.schedule;

import com.github.lucbui.fracktail3.spring.annotation.schedule.strategy.InjectPlatformStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ParameterScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterScheduleStrategy(InjectPlatformStrategy.class)
public @interface InjectPlatform {
    String value() default "";
}

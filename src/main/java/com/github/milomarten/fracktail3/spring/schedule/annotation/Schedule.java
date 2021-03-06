package com.github.milomarten.fracktail3.spring.schedule.annotation;

import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.AutoOnExceptionCancelStrategy;
import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.AutoScheduleMethodStrategy;
import com.github.milomarten.fracktail3.spring.schedule.annotation.strategy.AutoScheduleReturnStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.ExceptionScheduleStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.MethodScheduleStrategy;
import com.github.milomarten.fracktail3.spring.schedule.plugin.ReturnScheduleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method or field as a scheduled action
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodScheduleStrategy(AutoScheduleMethodStrategy.class)
@ReturnScheduleStrategy(AutoScheduleReturnStrategy.class)
@ExceptionScheduleStrategy(AutoOnExceptionCancelStrategy.class)
public @interface Schedule {
    /**
     * The ID of the scheduled action. If empty, uses the method/field name
     * @return Scheduled action ID
     */
    String value() default "";
}

package com.github.lucbui.fracktail3.spring.schedule.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation that describes how an annotation can be used to decorate a ReturnComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ReturnScheduleStrategy {
    /**
     * The class of the ReturnComponentStrategy to use
     * @return The class of the ReturnComponentStrategy to use
     */
    Class<? extends ReturnScheduledComponentStrategy>[] value();
}

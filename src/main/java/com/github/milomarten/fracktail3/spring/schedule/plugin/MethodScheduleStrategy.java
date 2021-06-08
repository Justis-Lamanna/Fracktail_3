package com.github.milomarten.fracktail3.spring.schedule.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation that describes how an annotation can be used to decorate an ExceptionComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MethodScheduleStrategy {
    /**
     * The class of the ExceptionComponentStrategy to use
     * @return The class of the ExceptionComponentStrategy to use
     */
    Class<? extends MethodScheduledComponentStrategy>[] value();
}

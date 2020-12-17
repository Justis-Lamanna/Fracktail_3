package com.github.lucbui.fracktail3.spring.command.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation that describes how an annotation can be used to decorate a ParameterComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ParameterStrategy {
    /**
     * The class of the ParameterComponentStrategy to use
     * @return The class of the ParameterComponentStrategy to use
     */
    Class<? extends ParameterComponentStrategy>[] value();
}

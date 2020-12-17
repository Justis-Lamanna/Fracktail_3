package com.github.lucbui.fracktail3.spring.command.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A meta-annotation that describes how an annotation can be used to decorate a MethodComponent
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MethodStrategy {
    /**
     * The class of the MethodComponentStrategy to use
     * @return The class of the MethodComponentStrategy to use
     */
    Class<? extends MethodComponentStrategy>[] value();
}

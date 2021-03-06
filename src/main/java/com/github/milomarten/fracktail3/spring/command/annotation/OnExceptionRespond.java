package com.github.milomarten.fracktail3.spring.command.annotation;

import com.github.milomarten.fracktail3.spring.command.annotation.strategy.OnExceptionRespondStrategy;
import com.github.milomarten.fracktail3.spring.command.plugin.ExceptionStrategy;

import java.lang.annotation.*;

/**
 * Annotation which marks a basic Exception strategy, responding with a certain string when an exception is thrown.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Repeatable(OnExceptionRespond.Wrapper.class)
@ExceptionStrategy(OnExceptionRespondStrategy.class)
public @interface OnExceptionRespond {
    /**
     * The FormattedString to respond with
     * @return The FormattedString to respond with
     */
    String value();

    /**
     * One or more exceptions which should trigger this response.
     * If omitted, all exceptions will respond with this, unless a more specific handler exists
     * @return
     */
    Class<? extends Throwable>[] exception() default Throwable.class;

    /**
     * Wrapper class which allows this to be repeatable
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @interface Wrapper {
        OnExceptionRespond[] value();
    }
}

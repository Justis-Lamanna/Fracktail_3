package com.github.milomarten.fracktail3.spring.command.plugin;

import com.github.milomarten.fracktail3.spring.command.model.ExceptionComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Describes a strategy for decorating an ExceptionComponent
 */
public interface ExceptionComponentStrategy {
    /**
     * Decorate an ExceptionComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ExceptionComponent to decorate
     * @return The decorated ExceptionComponent
     */
    ExceptionComponent decorate(Object obj, Method method, ExceptionComponent base);

    /**
     * Decorate an ExceptionComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ExceptionComponent to decorate
     * @return The decorated ExceptionComponent
     */
    ExceptionComponent decorate(Object obj, Field field, ExceptionComponent base);
}

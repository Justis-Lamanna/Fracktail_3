package com.github.lucbui.fracktail3.spring.command.plugin;

import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Describes a strategy for creating and decorating an ReturnComponent
 */
public interface ReturnComponentStrategy {

    /**
     * Decorate an ReturnComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ParameterComponent to decorate
     * @return The decorated ParameterComponent
     */
    ReturnComponent decorate(Object obj, Method method, ReturnComponent base);

    /**
     * Decorate an ReturnComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ParameterComponent to decorate
     * @return The decorated ParameterComponent
     */
    ReturnComponent decorate(Object obj, Field field, ReturnComponent base);
}

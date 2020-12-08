package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Describes a strategy for creating and decorating an ReturnComponent
 */
public interface ReturnComponentStrategy {
    /**
     * Create a ReturnComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @return An Optional containing the created component, or empty if this strategy cannot handle.
     */
    Optional<ReturnComponent> create(Object obj, Method method);

    /**
     * Decorate an ReturnComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ParameterComponent to decorate
     * @return The decorated ParameterComponent
     */
    ReturnComponent decorate(Object obj, Method method, ReturnComponent base);

    /**
     * Create a ReturnComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @return An Optional containing the created component, or empty if this strategy cannot handle.
     */
    Optional<ReturnComponent> create(Object obj, Field field);

    /**
     * Decorate an ReturnComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ParameterComponent to decorate
     * @return The decorated ParameterComponent
     */
    ReturnComponent decorate(Object obj, Field field, ReturnComponent base);
}

package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Describes a strategy for decorating an MethodComponent
 */
public interface MethodComponentStrategy {
    /**
     * Decorate an MethodComponent
     * @param obj The bean object being compiled
     * @param method The method being compiled
     * @param base The base MethodComponent to decorate
     * @return The decorated MethodComponent
     */
    MethodComponent decorate(Object obj, Method method, MethodComponent base);

    /**
     * Decorate an MethodComponent
     * @param obj The bean object being compiled
     * @param field The field being compiled
     * @param base The base MethodComponent to decorate
     * @return The decorated MethodComponent
     */
    MethodComponent decorate(Object obj, Field field, MethodComponent base);
}

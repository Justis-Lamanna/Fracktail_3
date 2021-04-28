package com.github.lucbui.fracktail3.spring.command.plugin;

import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Describes a strategy for creating and decorating an ParameterComponent
 */
public interface ParameterComponentStrategy {
    /**
     * Decorate an ParameterComponent
     * @param obj The bean object
     * @param method The method being called for this action
     * @param parameter The parameter being injected for this action
     * @param base The base ParameterComponent to decorate
     * @return The decorated ParameterComponent
     */
    ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base);
}

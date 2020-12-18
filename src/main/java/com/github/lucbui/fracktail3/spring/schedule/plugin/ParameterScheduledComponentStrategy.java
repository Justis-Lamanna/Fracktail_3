package com.github.lucbui.fracktail3.spring.schedule.plugin;

import com.github.lucbui.fracktail3.spring.schedule.model.ParameterScheduledComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * Describes a strategy for creating and decorating an ParameterComponent
 */
public interface ParameterScheduledComponentStrategy {
    /**
     * Create a ParameterScheduledComponent
     * @param obj The bean object
     * @param method The method being called for this action
     * @param parameter The parameter being injected for this action
     * @return An Optional containing the created component, or empty if this strategy cannot handle.
     */
    Optional<ParameterScheduledComponent> createSchedule(Object obj, Method method, Parameter parameter);

    /**
     * Decorate an ParameterScheduledComponent
     * @param obj The bean object
     * @param method The method being called for this action
     * @param parameter The parameter being injected for this action
     * @param base The base ParameterScheduledComponent to decorate
     * @return The decorated ParameterScheduledComponent
     */
    ParameterScheduledComponent decorateSchedule(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base);
}
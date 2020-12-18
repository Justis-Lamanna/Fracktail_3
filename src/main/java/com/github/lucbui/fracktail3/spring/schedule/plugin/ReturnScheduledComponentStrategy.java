package com.github.lucbui.fracktail3.spring.schedule.plugin;

import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Describes a strategy for creating and decorating an ReturnComponent
 */
public interface ReturnScheduledComponentStrategy {
    /**
     * Create a ReturnScheduledComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @return An Optional containing the created component, or empty if this strategy cannot handle.
     */
    Optional<ReturnScheduledComponent> createSchedule(Object obj, Method method);

    /**
     * Decorate an ReturnScheduledComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ReturnScheduledComponent to decorate
     * @return The decorated ReturnScheduledComponent
     */
    ReturnScheduledComponent decorateSchedule(Object obj, Method method, ReturnScheduledComponent base);

    /**
     * Create a ReturnScheduledComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @return An Optional containing the created component, or empty if this strategy cannot handle.
     */
    Optional<ReturnScheduledComponent> createSchedule(Object obj, Field field);

    /**
     * Decorate an ReturnScheduledComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ReturnScheduledComponent to decorate
     * @return The decorated ReturnScheduledComponent
     */
    ReturnScheduledComponent decorateSchedule(Object obj, Field field, ReturnScheduledComponent base);
}
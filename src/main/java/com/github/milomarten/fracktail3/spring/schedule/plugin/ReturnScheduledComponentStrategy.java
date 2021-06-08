package com.github.milomarten.fracktail3.spring.schedule.plugin;

import com.github.milomarten.fracktail3.spring.schedule.model.ReturnScheduledComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Describes a strategy for creating and decorating an ReturnComponent
 */
public interface ReturnScheduledComponentStrategy {
    /**
     * Decorate an ReturnScheduledComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ReturnScheduledComponent to decorate
     * @return The decorated ReturnScheduledComponent
     */
    ReturnScheduledComponent decorateSchedule(Object obj, Method method, ReturnScheduledComponent base);

    /**
     * Decorate an ReturnScheduledComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ReturnScheduledComponent to decorate
     * @return The decorated ReturnScheduledComponent
     */
    ReturnScheduledComponent decorateSchedule(Object obj, Field field, ReturnScheduledComponent base);
}

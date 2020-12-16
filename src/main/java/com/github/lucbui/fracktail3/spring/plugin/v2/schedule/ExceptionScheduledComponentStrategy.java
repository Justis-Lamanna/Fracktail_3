package com.github.lucbui.fracktail3.spring.plugin.v2.schedule;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionScheduledComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Describes a strategy for decorating an ExceptionScheduledComponent
 */
public interface ExceptionScheduledComponentStrategy {
    /**
     * Decorate an ExceptionScheduledComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @param base The base ExceptionScheduledComponent to decorate
     * @return The decorated ExceptionScheduledComponent
     */
    ExceptionScheduledComponent decorateSchedule(Object obj, Method method, ExceptionScheduledComponent base);

    /**
     * Decorate an ExceptionScheduledComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @param base The base ExceptionScheduledComponent to decorate
     * @return The decorated ExceptionScheduledComponent
     */
    ExceptionScheduledComponent decorateSchedule(Object obj, Field field, ExceptionScheduledComponent base);
}

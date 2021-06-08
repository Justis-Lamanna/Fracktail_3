package com.github.milomarten.fracktail3.spring.schedule.plugin;

import com.github.milomarten.fracktail3.spring.schedule.model.MethodScheduledComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface MethodScheduledComponentStrategy {
    MethodScheduledComponent decorateSchedule(Object obj, Method method, MethodScheduledComponent base);
    MethodScheduledComponent decorateSchedule(Object obj, Field field, MethodScheduledComponent base);
}

package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.annotation.Payload;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.schedule.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ParameterScheduledComponentStrategy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class PayloadStrategy implements ParameterComponentStrategy, ParameterScheduledComponentStrategy {
    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(Payload.class)) {
            return Optional.of(new ParameterComponent(BaseContext::getPayload));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }

    @Override
    public Optional<ParameterScheduledComponent> createSchedule(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(Payload.class)) {
            return Optional.of(new ParameterScheduledComponent(BaseContext::getPayload));
        }
        return Optional.empty();
    }

    @Override
    public ParameterScheduledComponent decorateSchedule(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base) {
        return base;
    }
}

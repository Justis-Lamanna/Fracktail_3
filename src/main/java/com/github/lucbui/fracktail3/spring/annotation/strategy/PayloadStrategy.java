package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.spring.annotation.Payload;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ParameterScheduledComponentStrategy;

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

package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.spring.annotation.Platform;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class PlatformStrategy implements ParameterComponentStrategy {
    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(Platform.class)) {
            return Optional.of(new ParameterComponent(PlatformBaseContext::getPlatform));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }
}

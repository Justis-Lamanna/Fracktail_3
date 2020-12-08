package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public interface ParameterComponentStrategy {
    Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter);

    ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base);
}

package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Component
public class ParameterStrategy implements ParameterComponentStrategy {
    @Autowired
    private ParameterConverters converters;

    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
            com.github.lucbui.fracktail3.spring.annotation.Parameter pAnnot =
                    parameter.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Parameter.class);
            int value = pAnnot.value();
            Class<?> paramType = parameter.getType();

            return Optional.of(new ParameterComponent(new ParameterToObjectConverterFunction(paramType, value, converters)));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }
}

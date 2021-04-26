package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
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
        if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class)) {
            com.github.lucbui.fracktail3.spring.command.annotation.Parameter pAnnot =
                    parameter.getAnnotation(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class);
            int value = pAnnot.value();
            Class<?> paramType = parameter.getType();
            TypeDescriptor descriptor = new TypeDescriptor(MethodParameter.forParameter(parameter));

            ParameterComponent component = new ParameterComponent(
                    descriptor,
                    new ParameterToObjectConverterFunction(paramType, value, converters),
                    StringUtils.defaultIfEmpty(pAnnot.name(), parameter.getName()));
            component.setHelp(pAnnot.description());
            component.setOptional(pAnnot.optional());
            return Optional.of(component);
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }
}

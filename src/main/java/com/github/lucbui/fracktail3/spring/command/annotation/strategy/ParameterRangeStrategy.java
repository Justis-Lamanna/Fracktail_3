package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToArrayConverterFunction;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToStringConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Component
public class ParameterRangeStrategy implements ParameterComponentStrategy {
    @Autowired
    private ParameterConverters converters;

    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(ParameterRange.class)) {
            return Optional.of(compileParameterRangeAnnotated(obj, method, parameter));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }

    protected ParameterComponent compileParameterRangeAnnotated(Object obj, Method method, Parameter parameter) {
        ParameterRange range = parameter.getAnnotation(ParameterRange.class);
        int start = range.lower();
        int end = range.upper();
        Class<?> paramType = parameter.getType();

        if(paramType.isArray()) {
            Class<?> innerType = paramType.getComponentType();
            return new ParameterComponent(new ParameterRangeToArrayConverterFunction(start, end, innerType, converters));
        } else if(ClassUtils.isAssignable(String.class, paramType)) {
            if(start == 0 && end == -1) {
                return new ParameterComponent(new ParameterRangeToStringConverterFunction());
            } else {
                throw new BotConfigurationException("Cannot convert parameters to type String unless start and end are unspecified");
            }
        } else {
            throw new BotConfigurationException("Cannot convert parameters to type " + paramType.getCanonicalName());
        }
    }
}

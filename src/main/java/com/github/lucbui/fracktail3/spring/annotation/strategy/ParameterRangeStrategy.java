package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterConverters;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToArrayHandler;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterRangeToStringHandler;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;
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
            return new ParameterComponent(new ParameterRangeToArrayHandler(start, end, innerType, converters));
        } else if(ClassUtils.isAssignable(String.class, paramType)) {
            if(start == 0 && end == -1) {
                return new ParameterComponent(new ParameterRangeToStringHandler());
            } else {
                throw new BotConfigurationException("Cannot convert parameters to type String unless start and end are unspecified");
            }
        } else {
            throw new BotConfigurationException("Cannot convert parameters to type " + paramType.getCanonicalName());
        }
    }
}

package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.spring.annotation.Variable;
import com.github.lucbui.fracktail3.spring.command.handler.VariableToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ParameterScheduledComponentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Component
public class VariableStrategy implements ParameterComponentStrategy, ParameterScheduledComponentStrategy {
    @Autowired
    private ParameterConverters converters;

    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(Variable.class)) {
            Variable annot = parameter.getAnnotation(Variable.class);
            return Optional.of(new ParameterComponent(new VariableToObjectConverterFunction(parameter.getType(), annot.value(), converters)));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }

    @Override
    public Optional<ParameterScheduledComponent> createSchedule(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(Variable.class)) {
            Variable annot = parameter.getAnnotation(Variable.class);
            return Optional.of(new ParameterScheduledComponent(new VariableToObjectConverterFunction(parameter.getType(), annot.value(), converters)));
        }
        return Optional.empty();
    }

    @Override
    public ParameterScheduledComponent decorateSchedule(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base) {
        return base;
    }
}

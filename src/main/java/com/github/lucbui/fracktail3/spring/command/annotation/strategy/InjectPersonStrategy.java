package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.InjectPerson;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class InjectPersonStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectPersonStrategy.class);

    @Override
    public Optional<ParameterComponent> create(Object obj, Method method, Parameter parameter) {
        if(parameter.isAnnotationPresent(InjectPerson.class)) {
            LOGGER.debug("+-Injecting sender");
            return Optional.of(new ParameterComponent(new TypeDescriptor(MethodParameter.forParameter(parameter)), CommandUseContext::getSender));
        }
        return Optional.empty();
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }
}

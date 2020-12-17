package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ReturnComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ReturnConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class AutoReturnStrategy implements ReturnComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReturnStrategy.class);

    @Autowired
    private ReturnConverters converters;

    @Override
    public Optional<ReturnComponent> create(Object obj, Method method) {
        return converters.getHandlerForType(method.getReturnType())
                .map(ReturnComponent::new);
    }

    @Override
    public ReturnComponent decorate(Object obj, Method method, ReturnComponent base) {
        return base;
    }

    @Override
    public Optional<ReturnComponent> create(Object obj, Field field) {
        return converters.getHandlerForType(field.getType())
                .map(ReturnComponent::new);
    }

    @Override
    public ReturnComponent decorate(Object obj, Field field, ReturnComponent base) {
        return base;
    }
}

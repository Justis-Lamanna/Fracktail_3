package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ReturnComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ReturnConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
@Order(0)
public class AutoReturnStrategy implements ReturnComponentStrategy {
    @Autowired
    private ReturnConverters converters;

    @Override
    public ReturnComponent decorate(Object obj, Method method, ReturnComponent base) {
        base.setFunc(converters.getHandlerForType(new TypeDescriptor(new MethodParameter(method, -1)))
                .orElseThrow(() -> new BotConfigurationException("Unable to find handler for type " + method.getReturnType())));
        return base;
    }

    @Override
    public ReturnComponent decorate(Object obj, Field field, ReturnComponent base) {
        base.setFunc(converters.getHandlerForType(new TypeDescriptor(field))
                .orElseThrow(() -> new BotConfigurationException("Unable to find handler for type " + field.getType())));
        return base;
    }
}

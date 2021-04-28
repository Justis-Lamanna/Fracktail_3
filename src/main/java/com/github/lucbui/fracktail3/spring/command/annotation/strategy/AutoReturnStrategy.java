package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ReturnComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ReturnConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class AutoReturnStrategy implements ReturnComponentStrategy {
    @Autowired
    private ReturnConverters converters;

    @Override
    public ReturnComponent decorate(Object obj, Method method, ReturnComponent base) {
        base.setFunc(converters.getHandlerForType(method.getReturnType())
                .orElseThrow(() -> new BotConfigurationException("Unable to find handler for type " + method.getReturnType())));
        return base;
    }

    @Override
    public ReturnComponent decorate(Object obj, Field field, ReturnComponent base) {
        base.setFunc(converters.getHandlerForType(field.getType())
                .orElseThrow(() -> new BotConfigurationException("Unable to find handler for type " + field.getType())));
        return base;
    }
}

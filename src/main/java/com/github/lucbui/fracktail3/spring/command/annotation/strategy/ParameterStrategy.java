package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
public class ParameterStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterStrategy.class);

    @Autowired
    private ParameterConverters converters;

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class)) {
            com.github.lucbui.fracktail3.spring.command.annotation.Parameter pAnnot =
                    parameter.getAnnotation(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class);
            int value = pAnnot.value();
            Class<?> paramType = parameter.getType();

            base.setFunc(new ParameterToObjectConverterFunction(paramType, value, converters));
            base.setName(StringUtils.defaultIfEmpty(pAnnot.name(), parameter.getName()));
            base.setHelp(StringUtils.defaultIfEmpty(pAnnot.description(), base.getName()));
            base.setOptional(pAnnot.optional());

            LOGGER.info("+-Parameter {},name:{},type:{},description:{},optional:{}", value,
                    base.getName(), base.getType().getResolvableType(), base.getHelp(), base.isOptional());
        }
        return base;
    }
}

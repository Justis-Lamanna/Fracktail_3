package com.github.milomarten.fracktail3.spring.command.annotation.strategy;

import com.github.milomarten.fracktail3.magic.params.ClassLimit;
import com.github.milomarten.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.milomarten.fracktail3.spring.command.model.ParameterComponent;
import com.github.milomarten.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.milomarten.fracktail3.spring.service.TypeLimitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Component
@Order(0)
public class ParameterStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterStrategy.class);

    @Autowired
    private TypeLimitService typeLimitService;

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        if(parameter.isAnnotationPresent(com.github.milomarten.fracktail3.spring.command.annotation.Parameter.class)) {
            com.github.milomarten.fracktail3.spring.command.annotation.Parameter pAnnot =
                    parameter.getAnnotation(com.github.milomarten.fracktail3.spring.command.annotation.Parameter.class);
            int value = pAnnot.value();
            TypeDescriptor paramType = new TypeDescriptor(MethodParameter.forParameter(parameter));

            base.setType(new ClassLimit(paramType).optional(parameter.getType() == Optional.class || pAnnot.optional()));
            base.setIndex(pAnnot.value());
            base.setName(StringUtils.defaultIfEmpty(pAnnot.name(), parameter.getName()));
            base.setHelp(StringUtils.defaultIfEmpty(pAnnot.description(), base.getName()));
            base.setFunc(new ParameterToObjectConverterFunction(value, typeLimitService));

            LOGGER.info("+-Parameter {},name:{},type:{},description:{}", value,
                    base.getName(), base.getType(), base.getHelp());
        }
        return base;
    }
}

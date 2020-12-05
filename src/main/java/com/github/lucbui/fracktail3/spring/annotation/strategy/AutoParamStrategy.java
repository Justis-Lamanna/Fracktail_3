package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.guard.ParameterSizeGuard;
import com.github.lucbui.fracktail3.spring.plugin.v2.MethodComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class AutoParamStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoParamStrategy.class);
    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        compileParameterSizeGuard(obj, method)
                .ifPresent(base::addGuard);
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        return base;
    }

    protected Optional<Guard> compileParameterSizeGuard(Object obj, Method method) {
        int min = 0;
        int max = 0;

        for(Parameter parameter : method.getParameters()) {
            if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
                com.github.lucbui.fracktail3.spring.annotation.Parameter annot =
                        parameter.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Parameter.class);
                if(annot.value() < 0) {
                    //At least -x parameters are needed (-1 means 1-infinite parameters are valid)
                    int minNumberOfParamsToSatisfy = -annot.value();
                    if(!annot.optional()) {
                        min = Math.max(min, minNumberOfParamsToSatisfy);
                    }
                    //Max can be any value above the minimum.
                } else {
                    //At least x + 1 parameters are needed (1 means 2 parameters are valid)
                    int numberOfParamsToSatisfy = annot.value() + 1;
                    if (!annot.optional()) {
                        min = Math.max(min, numberOfParamsToSatisfy);
                    }
                    max = Math.max(max, numberOfParamsToSatisfy);
                }
            }
            if(parameter.isAnnotationPresent(ParameterRange.class)) {
                ParameterRange annot = parameter.getAnnotation(ParameterRange.class);
                if(annot.upper() >= 0 && annot.upper() < annot.lower()) {
                    throw new BotConfigurationException("@ParameterRange is invalid range: " + annot.lower() + "-" + annot.upper());
                }
                if(annot.upper() < 0) {
                    int minNumberOfParamsToSatisfy;
                    if(annot.lower() == -1 && annot.upper() == -1) {
                        minNumberOfParamsToSatisfy = 0;
                    } else {
                        minNumberOfParamsToSatisfy = Math.max(-annot.upper(), annot.lower() + 1);
                    }
                    if (!annot.optional()) {
                        min = Math.max(min, minNumberOfParamsToSatisfy);
                    }
                } else {
                    int numberOfParamsToSatisfy = annot.upper() + 1;
                    if (!annot.optional()) {
                        min = Math.max(min, numberOfParamsToSatisfy);
                    }
                    max = Math.max(max, numberOfParamsToSatisfy);
                }
            }
        }

        if(max == 0) {
            max = Integer.MAX_VALUE;
        }

        LOGGER.debug("Calculated parameter count: min={}, max={}", min, max);
        //return Optional.empty();
        return Optional.of(new ParameterSizeGuard(min, max));
    }
}

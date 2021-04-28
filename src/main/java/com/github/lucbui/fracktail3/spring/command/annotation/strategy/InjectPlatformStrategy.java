package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.command.annotation.InjectPlatform;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.schedule.handler.InjectPlatformHandler;
import com.github.lucbui.fracktail3.spring.schedule.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ParameterScheduledComponentStrategy;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * A strategy which injects a specific Platform into this parameter
 */
public class InjectPlatformStrategy implements ParameterScheduledComponentStrategy, ParameterComponentStrategy {
    @Override
    public ParameterScheduledComponent decorateSchedule(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base) {
//        Class<? extends Platform> pType = validateParameterClass(parameter);
//        String key = getPlatformIdIfPresent(parameter);
//        base.setFunc(new InjectPlatformHandler(key, pType));
        return base;
    }

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        Class<? extends Platform> pType = validateParameterClass(parameter);
        String key = getPlatformIdIfPresent(parameter);
        base.setFunc(new InjectPlatformHandler(key, pType));
        return base;
    }

    private Class<? extends Platform> validateParameterClass(Parameter parameter) {
        if(!ClassUtils.isAssignable(parameter.getType(), Platform.class)) {
            throw new BotConfigurationException("Must use @InjectPlatform with Platform or subclass");
        }
        return (Class<? extends Platform>) parameter.getType();
    }

    private String getPlatformIdIfPresent(Parameter parameter) {
        InjectPlatform annot = parameter.getAnnotation(InjectPlatform.class);
        return StringUtils.defaultIfBlank(annot.value(), null);
    }
}

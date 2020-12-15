package com.github.lucbui.fracktail3.spring.annotation.schedule.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.annotation.schedule.InjectPlatform;
import com.github.lucbui.fracktail3.spring.command.handler.schedule.InjectPlatformHandler;
import com.github.lucbui.fracktail3.spring.command.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.plugin.v2.schedule.ParameterScheduledComponentStrategy;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * A strategy which injects a specific Platform into this parameter
 */
public class InjectPlatformStrategy implements ParameterScheduledComponentStrategy {
    @Override
    public Optional<ParameterScheduledComponent> create(Object obj, Method method, Parameter parameter) {
        if(!ClassUtils.isAssignable(parameter.getType(), Platform.class)) {
            throw new BotConfigurationException("Must use @InjectPlatform with Platform or subclass");
        }
        Class<? extends Platform> pType = (Class<? extends Platform>) parameter.getType();
        if(parameter.isAnnotationPresent(InjectPlatform.class)) {
            InjectPlatform annot = parameter.getAnnotation(InjectPlatform.class);
            String key = StringUtils.defaultIfBlank(annot.value(), null);
            return Optional.of(new ParameterScheduledComponent(new InjectPlatformHandler(key, pType)));
        }
        return Optional.empty();
    }

    @Override
    public ParameterScheduledComponent decorate(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base) {
        return base;
    }
}

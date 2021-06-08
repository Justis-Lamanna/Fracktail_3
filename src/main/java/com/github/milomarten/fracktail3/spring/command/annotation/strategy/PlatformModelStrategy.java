package com.github.milomarten.fracktail3.spring.command.annotation.strategy;

import com.github.milomarten.fracktail3.magic.platform.Platform;
import com.github.milomarten.fracktail3.spring.command.guard.PlatformValidatorGuard;
import com.github.milomarten.fracktail3.spring.command.model.MethodComponent;
import com.github.milomarten.fracktail3.spring.command.plugin.MethodComponentStrategy;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PlatformModelStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformModelStrategy.class);

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        Set<Class<? extends Platform>> platforms = getPlatformModelClasses(ResolvableType.forMethodReturnType(method), method.getParameters());
        if(!platforms.isEmpty()) {
            base.addGuard(new PlatformValidatorGuard(platforms));
        }
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        Set<Class<? extends Platform>> platforms = getPlatformModelClasses(ResolvableType.forField(field), null);
        if(!platforms.isEmpty()) {
            base.addGuard(new PlatformValidatorGuard(platforms));
        }
        return base;
    }

    private Set<Class<? extends Platform>> getPlatformModelClasses(ResolvableType returnType, Parameter[] parameters) {
        Set<Class<? extends Platform>> platforms = new HashSet<>(ArrayUtils.getLength(parameters) + 1);
        if(parameters != null) {
            for (Parameter p : parameters) {
                if(ClassUtils.isAssignable(p.getType(), Platform.class) && p.getType() != Platform.class) {
                    platforms.add((Class<Platform>)p.getType());
                }

                ResolvableType type = ResolvableType.forMethodParameter(MethodParameter.forParameter(p));
                Class<?> clazzToSearch = getClassToSearch(type);

                if (clazzToSearch != null && clazzToSearch.isAnnotationPresent(PlatformModel.class)) {
                    platforms.add(clazzToSearch.getAnnotation(PlatformModel.class).value());
                }
            }
        }

        Class<?> clazzToSearch = getClassToSearch(returnType);
        if(clazzToSearch != null && clazzToSearch.isAnnotationPresent(PlatformModel.class)) {
            platforms.add(clazzToSearch.getAnnotation(PlatformModel.class).value());
        }

        LOGGER.debug("+-Limiting command to usage with platforms of type(s) {}", platforms);
        return platforms;
    }

    private Class<?> getClassToSearch(ResolvableType type) {
        ResolvableType cast;
        if(type.isArray()) {
            return getClassToSearch(type.getComponentType());
        } else if((cast = type.asMap()) != ResolvableType.NONE) {
            // For maps, we investigate the value type
            return getClassToSearch(cast.getGeneric(1));
        } else if((cast = type.asCollection()) != ResolvableType.NONE) {
            // For lists, investigate the collection type
            return getClassToSearch(cast.getGeneric(0));
        } else if((cast = type.as(Optional.class)) != ResolvableType.NONE) {
            // For optionals, investigate the optional type
            return getClassToSearch(cast.getGeneric(0));
        } else {
            return type.resolve();
        }
    }
}

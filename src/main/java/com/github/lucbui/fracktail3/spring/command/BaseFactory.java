package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.util.Defaults;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class BaseFactory {
    protected final ConversionService conversionService;
    protected final Plugins plugins;

    public BaseFactory(ConversionService conversionService, Plugins plugins) {
        this.conversionService = conversionService;
        this.plugins = plugins;
    }

    protected Object convertObjectForParam(Object obj, Parameter param) {
        return convertObjectForParamUsingClass(obj, param.getType());
    }

    protected <T> T convertObjectForParamUsingClass(Object obj, Class<T> paramType) {
        if(obj == null) {
            return Defaults.getDefault(paramType);
        } else if(ClassUtils.isAssignable(obj.getClass(), paramType)) {
            return (T) obj;
        } else if(paramType.equals(Optional.class)) {
            return (T) Optional.of(obj); //All we can do is assume the types are right.
        } else if(conversionService.canConvert(obj.getClass(), paramType)) {
            return conversionService.convert(obj, paramType);
        } else {
            return Defaults.getDefault(paramType);
        }
    }

    protected boolean isNotOptional(Parameter parameter, boolean optionalFromAnnotation) {
        Class<?> clazz = parameter.getType();
        return !optionalFromAnnotation && isNotOptional(clazz);
    }

    protected boolean isNotOptional(Class<?> clazz) {
        return !clazz.equals(Optional.class) && !clazz.equals(OptionalInt.class) &&
                !clazz.equals(OptionalLong.class) && !clazz.equals(OptionalDouble.class);
    }
}

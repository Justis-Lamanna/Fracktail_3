package com.github.lucbui.fracktail3.spring.command.service;

import com.github.lucbui.fracktail3.spring.util.Defaults;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParameterConverters {
    @Autowired
    private ConversionService conversionService;

    public <T> T convertToType(Object obj, Class<T> paramType) {
        if(obj == null) {
            return Defaults.getDefault(paramType);
        } else if(ClassUtils.isAssignable(obj.getClass(), paramType)) {
            return (T) obj;
        } else if(paramType.equals(Optional.class)) {
            return (T) Optional.of(obj); //All we can do is assume the types are right.
        } else if(conversionService.canConvert(obj.getClass(), paramType)) {
            return conversionService.convert(obj, paramType);
        } else {
            throw new ClassCastException("Cannot convert obj " + obj + " to param type " + paramType.getCanonicalName());
        }
    }
}

package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.plugin.v2.*;
import com.github.lucbui.fracktail3.spring.util.Defaults;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.core.convert.ConversionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    protected List<MethodComponentStrategy> getMethodStrategies(AnnotatedElement element) {
        return getStrategies(element, MethodStrategy.class, MethodStrategy::value);
    }

    protected List<ExceptionComponentStrategy> getExceptionStrategies(AnnotatedElement element) {
        return getStrategies(element, ExceptionStrategy.class, ExceptionStrategy::value);
    }

    protected List<ParameterComponentStrategy> getParameterStrategies(AnnotatedElement element) {
        return getStrategies(element, ParameterStrategy.class, ParameterStrategy::value);
    }

    protected List<ReturnComponentStrategy> getReturnStrategies(AnnotatedElement element) {
        return getStrategies(element, ReturnStrategy.class, ReturnStrategy::value);
    }

    private <STRAT, ANNOT extends Annotation> List<STRAT> getStrategies(AnnotatedElement element,
                                                                        Class<ANNOT> annotation,
                                                                        Function<ANNOT, Class<? extends STRAT>[]> func) {
        return Arrays.stream(element.getAnnotations())
                .map(Annotation::annotationType)
                .filter(annotType -> annotType.isAnnotationPresent(annotation))
                .map(annotType -> annotType.getAnnotation(annotation))
                .flatMap(a -> Arrays.stream(func.apply(a)))
                .map(this::resolve)
                .collect(Collectors.toList());
    }

    private <T> T resolve(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BotConfigurationException("Error initializing MethodStrategy", e);
        }
    }
}

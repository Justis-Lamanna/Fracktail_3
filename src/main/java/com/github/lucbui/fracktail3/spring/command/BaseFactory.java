package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.plugin.v2.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseFactory implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
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
            return context.getBean(clazz);
        } catch (NoUniqueBeanDefinitionException ex) {
            throw new BotConfigurationException("Multiple beans of type " + clazz.getCanonicalName(), ex);
        } catch (NoSuchBeanDefinitionException ex) {
            try {
                return clazz.newInstance(); //TODO: Cache this
            } catch (InstantiationException | IllegalAccessException e) {
                throw new BotConfigurationException("Error initializing MethodStrategy", e);
            }
        } catch (BeansException ex) {
            throw new BotConfigurationException("Error creating bean " + clazz.getCanonicalName(), ex);
        }
    }
}
package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.annotation.OnExceptionRespond;
import com.github.lucbui.fracktail3.spring.command.handler.ExceptionRespondHandler;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ExceptionComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class OnExceptionRespondStrategy implements ExceptionComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnExceptionRespondStrategy.class);
    @Override
    public ExceptionComponent decorate(Object obj, Method method, ExceptionComponent base) {
        addHandlers(base, method.getDeclaringClass());
        addHandlers(base, method);
        return base;
    }

    @Override
    public ExceptionComponent decorate(Object obj, Field field, ExceptionComponent base) {
        addHandlers(base, field.getDeclaringClass());
        addHandlers(base, field);
        return base;
    }

    private void addHandlers(ExceptionComponent component, AnnotatedElement element) {
        Set<OnExceptionRespond> annotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(element,
                OnExceptionRespond.class, OnExceptionRespond.Wrapper.class);

        for(OnExceptionRespond annotation : annotations) {
            String response = annotation.value();
            ExceptionComponent.ExceptionHandler handler = new ExceptionRespondHandler(response);
            for(Class<? extends Throwable> clazz : annotation.exception()) {
                LOGGER.debug("+-On exception {} will respond with {}", clazz.getCanonicalName(), annotation.value());
                component.addHandler(clazz, handler);
            }
        }
    }
}

package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.service.StrategyExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A factory which compiles an object + method into an ExceptionComponent
 */
@Component
public class ExceptionComponentFactory {
    @Autowired
    private StrategyExtractor extractor;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    /**
     * Compile this object and method into an ExceptionComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public ExceptionComponent compileException(Object obj, Method method) {
        LOGGER.debug("Compiling exception handlers of method {}", method.getName());
        return BaseFactory.decorate(extractor.getExceptionStrategies(method),
                (strategy, component) -> strategy.decorate(obj, method, component), new ExceptionComponent());
    }

    /**
     * Compile this object and field into an ExceptionComponent
     * @param obj The bean object
     * @param field The field to compile
     * @return The created component
     */
    public ExceptionComponent compileException(Object obj, Field field) {
        LOGGER.debug("Compiling exception handlers of method {}", field.getName());
        return BaseFactory.decorate(extractor.getExceptionStrategies(field),
                (strategy, component) -> strategy.decorate(obj, field, component), new ExceptionComponent());
    }
}

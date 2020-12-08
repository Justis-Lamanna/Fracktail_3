package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.plugin.v2.ExceptionComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * A factory which compiles an object + method into an ExceptionComponent
 */
@Component
public class ExceptionComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    /**
     * Compile this object and method into an ExceptionComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public ExceptionComponent compileException(Object obj, Method method) {
        LOGGER.debug("Compiling exception handlers of method {}", method.getName());
        ExceptionComponent component = new ExceptionComponent();
        for(ExceptionComponentStrategy strategy : getExceptionStrategies(method)) {
            component = strategy.decorate(obj, method, component);
        }
        return component;
    }
}

package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.plugin.v2.MethodComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A factory which compiles an object + method, or object + field, into an MethodComponentFactory
 */
@Component
public class MethodComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    /**
     * Compile this object and method into a MethodComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public MethodComponent compileMethod(Object obj, Method method) {
        LOGGER.debug("Compiling method {}", method.getName());
        MethodComponent component = new MethodComponent();
        for(MethodComponentStrategy strategy : getMethodStrategies(method)) {
            component = strategy.decorate(obj, method, component);
        }
        return component;
    }

    /**
     * Compile this object and field into a MethodComponent
     * @param obj The bean object
     * @param field The method to compile
     * @return The created component
     */
    public MethodComponent compileField(Object obj, Field field) {
        LOGGER.debug("Compiling method {}", field.getName());
        MethodComponent component = new MethodComponent();
        for(MethodComponentStrategy strategy : getMethodStrategies(field)) {
            component = strategy.decorate(obj, field, component);
        }
        return component;
    }
}

package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.command.service.StrategyExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A factory which compiles an object and method, or object and field, into a ReturnComponent
 */
@Component
public class ReturnComponentFactory {
    @Autowired
    private StrategyExtractor extractor;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    /**
     * Compile an object and method into a ReturnComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @return The created ReturnComponent
     */
    public ReturnComponent compileReturn(Object obj, Method method) {
        LOGGER.debug("Compiling return of method {}", method.getName());
        return BaseFactory.createAndDecorate(
                extractor.getReturnStrategies(method),
                strategy -> strategy.create(obj, method),
                (strategy, component) -> strategy.decorate(obj, method, component)
        ).orElseThrow(() -> new BotConfigurationException("Unequipped to compile return for method " + method.getName()));
    }

    /**
     * Compile an object and method into a ReturnComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @return The created ReturnComponent
     */
    public ReturnComponent compileReturn(Object obj, Field field) {
        LOGGER.debug("Compiling return of field {}", field.getName());
        return BaseFactory.createAndDecorate(
                extractor.getReturnStrategies(field),
                strategy -> strategy.create(obj, field),
                (strategy, component) -> strategy.decorate(obj, field, component)
        ).orElseThrow(() -> new BotConfigurationException("Unequipped to compile return for field " + field.getName()));
    }
}

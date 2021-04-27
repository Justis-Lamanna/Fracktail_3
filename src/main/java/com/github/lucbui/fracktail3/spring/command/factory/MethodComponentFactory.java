package com.github.lucbui.fracktail3.spring.command.factory;

import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.MethodScheduledComponent;
import com.github.lucbui.fracktail3.spring.service.StrategyExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A factory which compiles an object + method, or object + field, into an MethodComponentFactory
 */
@Component
public class MethodComponentFactory {
    @Autowired
    private StrategyExtractor extractor;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    /**
     * Compile this object and method into a MethodComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public MethodComponent compileMethod(Object obj, Method method) {
        return FactoryUtils.decorate(extractor.getMethodStrategies(method),
                (strategy, component) -> strategy.decorate(obj, method, component), new MethodComponent());
    }

    /**
     * Compile this object and field into a MethodComponent
     * @param obj The bean object
     * @param field The method to compile
     * @return The created component
     */
    public MethodComponent compileField(Object obj, Field field) {
        return FactoryUtils.decorate(extractor.getMethodStrategies(field),
                (strategy, component) -> strategy.decorate(obj, field, component), new MethodComponent());
    }

    /**
     * Compile this object and field into a MethodComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public MethodScheduledComponent compileScheduleMethod(Object obj, Method method) {
        return FactoryUtils.decorate(extractor.getMethodScheduleStrategies(method),
                (strategy, component) -> strategy.decorateSchedule(obj, method, component), new MethodScheduledComponent());
    }

    /**
     * Compile this object and field into a MethodComponent
     * @param obj The bean object
     * @param field The field to compile
     * @return The created component
     */
    public MethodScheduledComponent compileScheduleMethod(Object obj, Field field) {
        return FactoryUtils.decorate(extractor.getMethodScheduleStrategies(field),
                (strategy, component) -> strategy.decorateSchedule(obj, field, component), new MethodScheduledComponent());
    }
}

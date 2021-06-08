package com.github.milomarten.fracktail3.spring.command.factory;

import com.github.milomarten.fracktail3.magic.exception.BotConfigurationException;
import com.github.milomarten.fracktail3.spring.command.model.ReturnComponent;
import com.github.milomarten.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import com.github.milomarten.fracktail3.spring.service.StrategyExtractor;
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
        ReturnComponent c = FactoryUtils.decorate(extractor.getReturnStrategies(method),
                (strategy, component) -> strategy.decorate(obj, method, component), new ReturnComponent());
        if(c.getFunc() == null) {
            throw new BotConfigurationException("Unable to parse return of method " + method.getName());
        }
        return c;
    }

    /**
     * Compile an object and method into a ReturnComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @return The created ReturnComponent
     */
    public ReturnComponent compileReturn(Object obj, Field field) {
        return FactoryUtils.decorate(extractor.getReturnStrategies(field),
                (strategy, component) -> strategy.decorate(obj, field, component), new ReturnComponent());
    }

    /**
     * Compile an object and method into a ReturnScheduledComponent
     * @param obj The bean object
     * @param method The method being compiled
     * @return The created ReturnScheduledComponent
     */
    public ReturnScheduledComponent compileScheduledReturn(Object obj, Method method) {
        ReturnScheduledComponent c = FactoryUtils.decorate(extractor.getReturnScheduleStrategies(method),
                (strategy, component) -> strategy.decorateSchedule(obj, method, component), new ReturnScheduledComponent());
        if(c.getFunc() == null) {
            throw new BotConfigurationException("Unable to parse return of method " + method.getName());
        }
        return c;
    }

    /**
     * Compile an object and method into a ReturnScheduledComponent
     * @param obj The bean object
     * @param field The field being compiled
     * @return The created ReturnScheduledComponent
     */
    public ReturnScheduledComponent compileScheduledReturn(Object obj, Field field) {
        return FactoryUtils.decorate(extractor.getReturnScheduleStrategies(field),
                (strategy, component) -> strategy.decorateSchedule(obj, field, component), new ReturnScheduledComponent());
    }
}

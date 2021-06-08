package com.github.milomarten.fracktail3.spring.command.factory;

import com.github.milomarten.fracktail3.magic.command.action.CommandAction;
import com.github.milomarten.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.milomarten.fracktail3.spring.command.model.ExceptionComponent;
import com.github.milomarten.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import com.github.milomarten.fracktail3.spring.service.StrategyExtractor;
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
        ExceptionComponent c = new ExceptionComponent();
        c.addHandler(CommandAction.class, CommandActionExceptionHandler.INSTANCE);
        return FactoryUtils.decorate(extractor.getExceptionStrategies(method),
                (strategy, component) -> strategy.decorate(obj, method, component), c);
    }

    /**
     * Compile this object and field into an ExceptionComponent
     * @param obj The bean object
     * @param field The field to compile
     * @return The created component
     */
    public ExceptionComponent compileException(Object obj, Field field) {
        ExceptionComponent c = new ExceptionComponent();
        c.addHandler(CommandAction.class, CommandActionExceptionHandler.INSTANCE);
        return FactoryUtils.decorate(extractor.getExceptionStrategies(field),
                (strategy, component) -> strategy.decorate(obj, field, component), c);
    }

    /**
     * Compile this object and method into an ExceptionComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return The created component
     */
    public ExceptionScheduledComponent compileScheduleException(Object obj, Method method) {
        ExceptionScheduledComponent c = new ExceptionScheduledComponent();
        c.addHandler(ScheduledAction.class, CommandActionExceptionHandler.INSTANCE);
        return FactoryUtils.decorate(extractor.getExceptionScheduleStrategies(method),
                (strategy, component) -> strategy.decorateSchedule(obj, method, component), new ExceptionScheduledComponent());
    }

    /**
     * Compile this object and field into an ExceptionComponent
     * @param obj The bean object
     * @param field The field to compile
     * @return The created component
     */
    public ExceptionScheduledComponent compileScheduleException(Object obj, Field field) {
        ExceptionScheduledComponent c = new ExceptionScheduledComponent();
        c.addHandler(ScheduledAction.class, CommandActionExceptionHandler.INSTANCE);
        return FactoryUtils.decorate(extractor.getExceptionScheduleStrategies(field),
                (strategy, component) -> strategy.decorateSchedule(obj, field, component), c);
    }
}

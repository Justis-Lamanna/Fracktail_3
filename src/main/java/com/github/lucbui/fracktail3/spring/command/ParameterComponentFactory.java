package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.command.service.StrategyExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A factory which compiles an object and method into its associated ParameterComponents
 */
@Component
public class ParameterComponentFactory {
    @Autowired
    private StrategyExtractor extractor;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterComponentFactory.class);

    /**
     * Compile an object and method into a list of ParameterComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return A list of ParameterComponent, one for each parameter in the method
     */
    public List<ParameterComponent> compileParameters(Object obj, Method method) {
        LOGGER.debug("Compiling parameters of method {}", method.getName());
        return Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    /**
     * Compile an object, method, and Parameter into a ParameterComponent
     * @param obj The bean object
     * @param method The method to compile
     * @param parameter The parameter to compile
     * @return A ParameterComponent
     */
    public ParameterComponent compileParameter(Object obj, Method method, Parameter parameter) {
        LOGGER.debug("Compiling parameter {} of method {}", parameter.getName(), method.getName());
        return BaseFactory.createAndDecorate(extractor.getParameterStrategies(parameter),
                strategy -> strategy.create(obj, method, parameter),
                (strategy, component) -> strategy.decorate(obj, method, parameter, component))
            .orElseThrow(() -> new BotConfigurationException("Unequipped to compile parameter " + parameter.getName() + " for method " + method.getName()));
    }

    /**
     * Compile an object and method into a list of ParameterComponent
     * @param obj The bean object
     * @param method The method to compile
     * @return A list of ParameterComponent, one for each parameter in the method
     */
    public List<ParameterScheduledComponent> compileScheduleParameters(Object obj, Method method) {
        LOGGER.debug("Compiling parameters of method {}", method.getName());
        return Arrays.stream(method.getParameters())
                .map(param -> compileScheduleParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    /**
     * Compile an object, method, and Parameter into a ParameterComponent
     * @param obj The bean object
     * @param method The method to compile
     * @param parameter The parameter to compile
     * @return A ParameterComponent
     */
    public ParameterScheduledComponent compileScheduleParameter(Object obj, Method method, Parameter parameter) {
        LOGGER.debug("Compiling parameter {} of method {}", parameter.getName(), method.getName());
        return BaseFactory.createAndDecorate(extractor.getParameterScheduleStrategies(parameter),
                strategy -> strategy.create(obj, method, parameter),
                (strategy, component) -> strategy.decorate(obj, method, parameter, component))
                .orElseThrow(() -> new BotConfigurationException("Unequipped to compile parameter " + parameter.getName() + " for method " + method.getName()));
    }
}

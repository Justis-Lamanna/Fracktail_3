package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.plugin.v2.ReturnComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Component
public class ReturnComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public ReturnComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public ReturnComponent compileReturn(Object obj, Method method) {
        LOGGER.debug("Compiling return of method {}", method.getName());
        List<ReturnComponentStrategy> strategies = getReturnStrategies(method);
        for(ReturnComponentStrategy s : strategies) {
            Optional<ReturnComponent> componentOpt = s.create(obj, method);
            if(componentOpt.isPresent()) {
                ReturnComponent component = componentOpt.get();
                for(ReturnComponentStrategy strategy : strategies) {
                    component = strategy.decorate(obj, method, component);
                }
                return component;
            }
        }
        throw new BotConfigurationException("Unequipped to compile return for method " + method.getName());
    }

    public ReturnComponent compileReturn(Object obj, Field field) {
        LOGGER.debug("Compiling return of field {}", field.getName());
        List<ReturnComponentStrategy> strategies = getReturnStrategies(field);
        for(ReturnComponentStrategy s : strategies) {
            Optional<ReturnComponent> componentOpt = s.create(obj, field);
            if(componentOpt.isPresent()) {
                ReturnComponent component = componentOpt.get();
                for(ReturnComponentStrategy strategy : strategies) {
                    component = strategy.decorate(obj, field, component);
                }
                return component;
            }
        }
        throw new BotConfigurationException("Unequipped to compile return for field " + field.getName());
    }
}

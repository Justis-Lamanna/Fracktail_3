package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.plugin.v2.MethodComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class MethodComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public MethodComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public MethodComponent compileMethod(Object obj, Method method) {
        LOGGER.debug("Compiling method {}", method.getName());
        MethodComponent component = new MethodComponent();
        for(MethodComponentStrategy strategy : getMethodStrategies(method)) {
            component = strategy.decorate(obj, method, component);
        }
        return component;
    }

    public MethodComponent compileField(Object obj, Field field) {
        LOGGER.debug("Compiling method {}", field.getName());
        MethodComponent component = new MethodComponent();
        for(MethodComponentStrategy strategy : getMethodStrategies(field)) {
            component = strategy.decorate(obj, field, component);
        }
        return component;
    }
}

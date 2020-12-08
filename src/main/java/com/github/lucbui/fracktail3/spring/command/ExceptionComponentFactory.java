package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.plugin.v2.ExceptionComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class ExceptionComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public ExceptionComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public ExceptionComponent compileException(Object obj, Method method) {
        LOGGER.debug("Compiling exception handlers of method {}", method.getName());
        ExceptionComponent component = new ExceptionComponent();
        for(ExceptionComponentStrategy strategy : getExceptionStrategies(method)) {
            component = strategy.decorate(obj, method, component);
        }
        return component;
    }
}

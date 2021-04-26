package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.spring.command.annotation.Command;
import com.github.lucbui.fracktail3.spring.command.annotation.Name;
import com.github.lucbui.fracktail3.spring.command.annotation.Usage;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodComponentStrategy;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

public class IdNameHelpStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdNameHelpStrategy.class);

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        if(method.isAnnotationPresent(Command.class)) {
            Command annot = method.getAnnotation(Command.class);
            base.setId(StringUtils.defaultIfEmpty(annot.value(), method.getName()));
            LOGGER.debug("Adding @Command-annotated method {}", base.getId());

            if(method.isAnnotationPresent(Name.class) && method.getAnnotation(Name.class).value().length > 0) {
                Name name = method.getAnnotation(Name.class);
                base.setNames(SetUtils.hashSet(name.value()));
            } else {
                base.setNames(Collections.singleton(StringUtils.defaultIfEmpty(base.getId(), method.getName())));
            }
            LOGGER.debug("+-Named as {}", base.getNames());

            if(method.isAnnotationPresent(Usage.class)) {
                Usage usage = method.getAnnotation(Usage.class);
                base.setHelp(usage.value());
                LOGGER.debug("+-With help text as \"{}\"", base.getHelp());
            }
        }
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        if(field.isAnnotationPresent(Command.class)) {
            Command annot = field.getAnnotation(Command.class);
            base.setId(StringUtils.defaultIfEmpty(annot.value(), field.getName()));
            LOGGER.debug("Adding @Command-annotated method {}", base.getId());

            if(field.isAnnotationPresent(Name.class) && field.getAnnotation(Name.class).value().length > 0) {
                Name name = field.getAnnotation(Name.class);
                base.setNames(SetUtils.hashSet(name.value()));
            } else {
                base.setNames(Collections.singleton(StringUtils.defaultIfEmpty(base.getId(), field.getName())));
            }
            LOGGER.debug("+-Named as {}", base.getNames());

            if(field.isAnnotationPresent(Usage.class)) {
                Usage usage = field.getAnnotation(Usage.class);
                base.setHelp(usage.value());
                LOGGER.debug("+-With help text as \"{}\"", base.getHelp());
            }
        }
        return base;
    }
}

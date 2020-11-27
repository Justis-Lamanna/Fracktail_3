package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.annotation.ParameterRange;
import com.github.lucbui.fracktail3.spring.command.guard.PlatformValidatorGuard;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

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

        compileParameterSizeGuard(obj, method).ifPresent(component::addGuard);

        if(method.isAnnotationPresent(ForPlatform.class)) {
            Class<? extends Platform> platform = method.getAnnotation(ForPlatform.class).value();
            LOGGER.debug("Limiting command to usage with {} platform", platform.getCanonicalName());
            component.addGuard(new PlatformValidatorGuard(platform));
        }
        return plugins.enhanceCompiledMethod(obj, method, component);
    }

    protected Optional<Guard> compileParameterSizeGuard(Object obj, Method method) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for(Parameter parameter : method.getParameters()) {
            if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
                com.github.lucbui.fracktail3.spring.annotation.Parameter annot =
                        parameter.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Parameter.class);
                if(!annot.optional()) {
                    if(annot.value() < 0) {
                        min = Math.min(min, -annot.value());
                    } else {
                        min = Math.min(min, annot.value());
                        max = Math.max(max, annot.value());
                    }
                }
            }
            if(parameter.isAnnotationPresent(ParameterRange.class)) {
                ParameterRange annot = parameter.getAnnotation(ParameterRange.class);
                if(!annot.optional()) {
                    if(annot.value() < 0) {
                        min = Math.min(min, -annot.value());
                    } else {
                        min = Math.min(min, annot.lower());
                        max = Math.max(max, annot.value());
                    }
                }
            }
        }

        if(min == Integer.MAX_VALUE) {
            min = max = 0;
        }
        if(max == Integer.MIN_VALUE) {
            max = Integer.MAX_VALUE;
        }
        LOGGER.debug("Calculated parameter count: min={}, max={}", min, max);
        return Optional.empty();
        //return Optional.of(new ParameterSizeGuard(min, max));
    }
}

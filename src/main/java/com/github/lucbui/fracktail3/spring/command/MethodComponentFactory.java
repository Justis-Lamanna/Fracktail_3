package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.annotation.Platform;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
        if(method.isAnnotationPresent(ForPlatform.class)) {
            Class<? extends Platform> platform = method.getAnnotation(ForPlatform.class).value();
            LOGGER.debug("Limiting command to usage with {} platform", platform.getCanonicalName());
            component.addGuard(ctx -> Mono.just(ClassUtils.isAssignable(ctx.getPlatform().getClass(), platform)));
        }
        return plugins.enhanceCompiledMethod(obj, method, component);
    }
}

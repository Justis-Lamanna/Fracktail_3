package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.command.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.command.guard.PlatformValidatorGuard;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Order(0)
public class ForPlatformStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForPlatformStrategy.class);

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        ForPlatform annot = method.getAnnotation(ForPlatform.class);
        base.addGuard(compileForPlatform(annot));
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        ForPlatform annot = field.getAnnotation(ForPlatform.class);
        base.addGuard(compileForPlatform(annot));
        return base;
    }

    protected Guard compileForPlatform(ForPlatform forPlatform) {
        Class<? extends Platform> platform = forPlatform.value();
        LOGGER.debug("+-Limiting command to usage with {} platform", platform.getCanonicalName());
        return new PlatformValidatorGuard(platform);
    }
}

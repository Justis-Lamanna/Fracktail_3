package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.annotation.strategy.ForPlatformStrategy;
import com.github.lucbui.fracktail3.spring.plugin.v2.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(ForPlatformStrategy.class)
public @interface ForPlatform {
    Class<? extends Platform> value();
}

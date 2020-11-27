package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.magic.platform.Platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ForPlatform {
    Class<? extends Platform> value();
}

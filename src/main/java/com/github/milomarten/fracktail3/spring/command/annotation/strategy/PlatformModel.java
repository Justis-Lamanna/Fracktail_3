package com.github.milomarten.fracktail3.spring.command.annotation.strategy;

import com.github.milomarten.fracktail3.magic.platform.Platform;
import com.github.milomarten.fracktail3.spring.command.annotation.ForPlatform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binds a model to a specific Platform.
 * In conjunction with the PlatformModelStrategy, this can automatically configure the platforms a command can
 * and cannot be used in.
 *
 * @see ForPlatform
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PlatformModel {
    /**
     * The platform this model belongs to
     * @return The class of the platform this model belongs to
     */
    Class<? extends Platform> value();
}

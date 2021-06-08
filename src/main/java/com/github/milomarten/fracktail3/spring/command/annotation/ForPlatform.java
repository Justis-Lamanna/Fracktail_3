package com.github.milomarten.fracktail3.spring.command.annotation;

import com.github.milomarten.fracktail3.magic.platform.Platform;
import com.github.milomarten.fracktail3.spring.command.annotation.strategy.ForPlatformStrategy;
import com.github.milomarten.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a command as only being valid for a specific platform
 * Instates a command guard which causes this command to be ignored when the wrong platform is used
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(ForPlatformStrategy.class)
public @interface ForPlatform {
    /**
     * The Platform class to permit
     * @return the Platform class to permit
     */
    Class<? extends Platform> value();
}

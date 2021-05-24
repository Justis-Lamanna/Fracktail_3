package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.RoleStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify this command can only be invoked by a person with the specified role.
 * This is the simplest of cases, where a command can only be invoked by a person with
 * one specific role.
 *
 * @see RoleExpression
 * @see Guarded
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(RoleStrategy.class)
public @interface Role {
    /**
     * The role the user must have to invoke this command
     * @return A role which the user must have
     */
    String value();
}

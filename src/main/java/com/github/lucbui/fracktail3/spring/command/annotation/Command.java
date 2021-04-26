package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.AutoReturnStrategy;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.IdNameHelpStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.ReturnStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method or field as a command
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(IdNameHelpStrategy.class)
@ReturnStrategy(AutoReturnStrategy.class)
public @interface Command {
    /**
     * The ID of the command. If empty, uses the method/field name
     * @return Command ID
     */
    String value() default "";
}

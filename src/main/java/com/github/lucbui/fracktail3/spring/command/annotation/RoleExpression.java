package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.spring.command.annotation.strategy.RoleExpressionStrategy;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specify more complex role logic for a command
 * The expression provided should be a SpEL expression. Inside the SpEL expression
 * two properties are exposed:
 * - roles: The set of roles the command user has
 * - ctx: The full context of the command use
 * Checks for a specific role can be done by using 'role.[role to check]', which
 * returns true or false depending on if the role is present. As an example, to check
 * if a user has the admin or owner role:
 * <pre>
 *     role.admin || role.owner
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@MethodStrategy(RoleExpressionStrategy.class)
public @interface RoleExpression {
    /**
     * SpEL expression returning true or false
     * @return Role expression
     */
    String value();
}

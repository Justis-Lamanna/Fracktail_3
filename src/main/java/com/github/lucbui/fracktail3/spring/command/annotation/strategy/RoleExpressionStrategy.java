package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.RoleExpression;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.RoleService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

@Component
public class RoleExpressionStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleExpressionStrategy.class);

    @Autowired
    private RoleService roleService;

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        RoleExpression roleExpression = method.getAnnotation(RoleExpression.class);
        Expression expression = PARSER.parseExpression(roleExpression.value());
        base.addGuard(new RoleExpressionGuard(expression));
        LOGGER.info("+-With role expression: {}", roleExpression.value());
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        RoleExpression roleExpression = field.getAnnotation(RoleExpression.class);
        Expression expression = PARSER.parseExpression(roleExpression.value());
        base.addGuard(new RoleExpressionGuard(expression));
        LOGGER.info("+-With role expression: {}", roleExpression.value());
        return base;
    }

    @Data
    private class RoleExpressionGuard implements Guard {
        private final Expression expression;

        @Override
        public Mono<Boolean> matches(CommandUseContext ctx) {
            return roleService.getRolesForPerson(ctx.getSender())
                    .map(roles -> {
                        StandardEvaluationContext evalContext = new StandardEvaluationContext(Collections.singletonMap("roles", roles));
                        return expression.getValue(evalContext, Boolean.class);
                    });
        }
    }
}

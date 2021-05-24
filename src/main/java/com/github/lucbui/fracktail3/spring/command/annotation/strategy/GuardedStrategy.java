package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.Guarded;
import com.github.lucbui.fracktail3.spring.command.handler.MonoPropertyAccessor;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.MethodComponentStrategy;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GuardedStrategy implements MethodComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuardedStrategy.class);
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        Guarded guarded = method.getAnnotation(Guarded.class);
        Expression expression = PARSER.parseExpression(guarded.value());
        base.addGuard(new ExpressionGuard(expression));
        LOGGER.info("+-With guard expression: {}", guarded.value());
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        Guarded guarded = field.getAnnotation(Guarded.class);
        Expression expression = PARSER.parseExpression(guarded.value());
        base.addGuard(new ExpressionGuard(expression));
        LOGGER.info("+-With guard expression: {}", guarded.value());
        return base;
    }

    @Data
    private static class ExpressionGuard implements Guard {
        private final Expression expression;

        @Override
        public Mono<Boolean> matches(CommandUseContext ctx) {
            return Mono.fromSupplier(() -> {
                StandardEvaluationContext evalContext = new StandardEvaluationContext(ctx);
                evalContext.addPropertyAccessor(MonoPropertyAccessor.INSTANCE);

                return expression.getValue(evalContext, Boolean.class);
            });
        }
    }
}

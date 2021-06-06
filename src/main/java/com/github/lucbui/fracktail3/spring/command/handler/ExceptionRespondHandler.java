package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * An ExceptionHandler which responds with a certain FormattedString
 */
public class ExceptionRespondHandler implements ExceptionComponent.ECFunction {
    private final Expression expression;

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final ParserContext CONTEXT = new ParserContext() {
        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return "#{";
        }

        @Override
        public String getExpressionSuffix() {
            return "}";
        }
    };

    /**
     * Initialize
     * @param fString The string to respond with
     */
    public ExceptionRespondHandler(String fString) {
        this.expression = PARSER.parseExpression(fString, CONTEXT);
    }

    @Override
    public Mono<Void> apply(CommandUseContext context, Throwable throwable) {
        Map<String, Object> inject = new HashMap<>(2);
        inject.put("context", context);
        inject.put("ex", throwable);
        return context.respond(expression.getValue(inject, String.class));
    }
}

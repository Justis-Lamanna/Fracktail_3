package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.parse.xml.ExpressionParser;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpringExpressionResolver implements ExpressionParser {
    private static final org.springframework.expression.ExpressionParser PARSER = new SpelExpressionParser();

    public final Environment environment;

    public SpringExpressionResolver(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String parseExpression(String value) {
        String resolvedToken = environment.resolvePlaceholders(value);
        final TemplateParserContext templateContext = new TemplateParserContext();
        Expression expression = PARSER.parseExpression(resolvedToken, templateContext);
        return expression.getValue(String.class);
    }
}

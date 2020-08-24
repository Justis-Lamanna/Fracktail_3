package com.github.lucbui.fracktail3.magic.parse.xml;

public class IdentityExpressionResolver implements ExpressionParser {
    @Override
    public String parseExpression(String value) {
        return value;
    }
}

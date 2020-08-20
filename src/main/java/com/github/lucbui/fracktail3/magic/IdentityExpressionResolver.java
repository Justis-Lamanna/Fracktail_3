package com.github.lucbui.fracktail3.magic;

public class IdentityExpressionResolver implements ExpressionResolver {
    @Override
    public String parseExpression(String value) {
        return value;
    }
}

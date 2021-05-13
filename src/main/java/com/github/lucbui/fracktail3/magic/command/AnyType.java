package com.github.lucbui.fracktail3.magic.command;

public class AnyType implements TypeLimits {
    public static final AnyType INSTANCE = new AnyType();

    private AnyType() {
    }

    @Override
    public boolean matches(Object obj) {
        return true;
    }
}

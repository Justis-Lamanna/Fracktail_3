package com.github.lucbui.fracktail3.magic.command.params;

public enum AnyType implements TypeLimits {
    INSTANCE;

    @Override
    public boolean matches(Object obj) {
        return true;
    }
}

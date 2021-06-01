package com.github.lucbui.fracktail3.magic.params;

/**
 * A TypeLimit which says that any object is legal
 */
public enum AnyType implements TypeLimits {
    INSTANCE;

    @Override
    public boolean matches(Object obj) {
        return true;
    }
}
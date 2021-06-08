package com.github.milomarten.fracktail3.magic.params;

/**
 * A TypeLimit which says that any object is legal
 */
public enum AnyType implements TypeLimits {
    INSTANCE;

    @Override
    public boolean matches(Object obj) {
        return true;
    }

    @Override
    public TypeLimits optional(boolean opt) {
        return this;
    }
}

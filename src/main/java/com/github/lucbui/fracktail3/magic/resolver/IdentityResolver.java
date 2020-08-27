package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.Locale;

public class IdentityResolver<T> implements Resolver<T> {
    private final T returned;

    public IdentityResolver(T returned) {
        this.returned = returned;
    }

    @Override
    public T resolve(Config configuration, Locale locale) {
        return returned;
    }

    public T getReturned() {
        return returned;
    }

    @Override
    public String toString() {
        return "IdentityResolver{" +
                "returned=" + returned +
                '}';
    }
}

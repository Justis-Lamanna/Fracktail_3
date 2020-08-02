package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.Locale;

public interface Resolver<T> {
    T resolve(Config configuration, Locale locale);

    static <I> Resolver<I> identity(I value) {
        return new IdentityResolver<>(value);
    }
}

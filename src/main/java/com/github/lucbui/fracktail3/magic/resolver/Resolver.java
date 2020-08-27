package com.github.lucbui.fracktail3.magic.resolver;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.Locale;

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
public interface Resolver<T> {
    T resolve(Config configuration, Locale locale);

    static <I> Resolver<I> identity(I value) {
        return new IdentityResolver<>(value);
    }
}

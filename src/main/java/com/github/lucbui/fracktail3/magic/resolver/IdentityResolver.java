package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.Locale;

/**
 * A resolver which simply returns a constant
 * @param <T> The type of the constant
 */
public class IdentityResolver<T> implements Resolver<T> {
    private final T returned;

    /**
     * Initialize a resolver with a constant
     * @param returned The constant to return when this is called
     */
    public IdentityResolver(T returned) {
        this.returned = returned;
    }

    @Override
    public T resolve(Config configuration, Locale locale) {
        return returned;
    }

    /**
     * Get the constant returned
     * @return The constant
     */
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

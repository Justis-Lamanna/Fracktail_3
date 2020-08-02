package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CompositeResolver<T> implements Resolver<List<T>> {
    private final List<? extends Resolver<? extends T>> resolvers;

    public CompositeResolver(List<? extends Resolver<? extends T>> resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public List<T> resolve(Config configuration, Locale locale) {
        return resolvers.stream()
                .map(r -> r.resolve(configuration, locale))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    @Override
    public String toString() {
        return "CompositeResolver{" +
                "resolvers=" + resolvers +
                '}';
    }
}

package com.github.lucbui.fracktail3.magic.utils;

import reactor.core.publisher.Mono;

/**
 * Utilities for dealing with Monos
 */
public class MonoUtils {
    private MonoUtils(){}

    /**
     * Return a Mono that is the the provided two ANDed
     * @param first The first mono
     * @param second The second mono
     * @return The two monos ANDed together
     */
    public static Mono<Boolean> and(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? second : Mono.just(false));
    }

    /**
     * Return a Mono that is the provided two or more Monos ANDed
     * @param first The first Mono
     * @param second The second Mono
     * @param more The remaining Monos
     * @return All monos ANDed together
     */
    @SafeVarargs
    public static Mono<Boolean> and(Mono<Boolean> first, Mono<Boolean> second, Mono<Boolean>... more) {
        Mono<Boolean> current = and(first, second);
        for(Mono<Boolean> next : more) {
            current = and(current, next);
        }
        return current;
    }

    /**
     * Return a Mono that is the provided two ORed
     * @param first The first Mono
     * @param second The second Mono
     * @return All monos ORed together
     */
    public static Mono<Boolean> or(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? Mono.just(true) : second);
    }

    /**
     * Return a Mono that is the inversion of the provided
     * @param mono The mono to NOT
     * @return The NOTed mono
     */
    public static Mono<Boolean> not(Mono<Boolean> mono) {
        return mono.map(b -> !b);
    }
}

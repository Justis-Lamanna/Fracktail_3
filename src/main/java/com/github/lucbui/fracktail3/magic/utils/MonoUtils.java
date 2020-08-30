package com.github.lucbui.fracktail3.magic.utils;

import reactor.core.publisher.Mono;

public class MonoUtils {
    public static Mono<Boolean> and(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? second : Mono.just(false));
    }

    @SafeVarargs
    public static Mono<Boolean> and(Mono<Boolean> first, Mono<Boolean> second, Mono<Boolean>... more) {
        Mono<Boolean> current = and(first, second);
        for(Mono<Boolean> next : more) {
            current = and(current, next);
        }
        return current;
    }

    public static Mono<Boolean> or(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? Mono.just(true) : second);
    }

    public static Mono<Boolean> not(Mono<Boolean> mono) {
        return mono.map(b -> !b);
    }
}

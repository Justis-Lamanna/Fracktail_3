package com.github.lucbui.fracktail3.magic.utils;

import reactor.core.publisher.Mono;

public class MonoUtils {
    public static Mono<Boolean> and(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? second : Mono.just(false));
    }

    public static Mono<Boolean> or(Mono<Boolean> first, Mono<Boolean> second) {
        return first.flatMap(b -> b ? Mono.just(true) : second);
    }

    public static Mono<Boolean> not(Mono<Boolean> mono) {
        return mono.map(b -> !b);
    }
}

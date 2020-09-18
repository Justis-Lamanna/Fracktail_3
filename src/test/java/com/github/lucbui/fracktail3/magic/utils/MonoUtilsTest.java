package com.github.lucbui.fracktail3.magic.utils;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MonoUtilsTest {
    @Test
    void testAnd_True() {
        assertTrue(MonoUtils.and(Mono.just(true), Mono.just(true)).block());
    }

    @Test
    void testAnd_False() {
        assertFalse(MonoUtils.and(Mono.just(true), Mono.just(false)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(true)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(false)).block());
    }

    @Test
    void testMultiAnd_True() {
        assertTrue(MonoUtils.and(Mono.just(true), Mono.just(true), Mono.just(true)).block());
    }

    @Test
    void testMultiAnd_False() {
        assertFalse(MonoUtils.and(Mono.just(true), Mono.just(true), Mono.just(false)).block());
        assertFalse(MonoUtils.and(Mono.just(true), Mono.just(false), Mono.just(true)).block());
        assertFalse(MonoUtils.and(Mono.just(true), Mono.just(false), Mono.just(false)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(true), Mono.just(true)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(true), Mono.just(false)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(false), Mono.just(true)).block());
        assertFalse(MonoUtils.and(Mono.just(false), Mono.just(false), Mono.just(false)).block());
    }

    @Test
    void testOr_True() {
        assertTrue(MonoUtils.or(Mono.just(true), Mono.just(true)).block());
        assertTrue(MonoUtils.or(Mono.just(true), Mono.just(false)).block());
        assertTrue(MonoUtils.or(Mono.just(false), Mono.just(true)).block());
    }

    @Test
    void testOr_False() {
        assertFalse(MonoUtils.or(Mono.just(false), Mono.just(false)).block());
    }

    @Test
    void testNot_TrueToFalse() {
        assertFalse(MonoUtils.not(Mono.just(true)).block());
    }

    @Test
    void testNot_FalseToTrue() {
        assertTrue(MonoUtils.not(Mono.just(false)).block());
    }
}
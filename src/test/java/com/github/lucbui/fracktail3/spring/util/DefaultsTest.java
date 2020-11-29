package com.github.lucbui.fracktail3.spring.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;

class DefaultsTest {
    @Test
    void defaultIntegerReturnZero() {
        assertEquals(0, Defaults.getDefault(Integer.TYPE));
    }

    @Test
    void defaultLongReturnZero() {
        assertEquals(0, Defaults.getDefault(Long.TYPE));
    }

    @Test
    void defaultDoubleReturnZero() {
        assertEquals(0, Defaults.getDefault(Double.TYPE));
    }

    @Test
    void defaultOptionalReturnsEmpty() {
        Optional<?> opt = Defaults.getDefault(Optional.class);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test
    void defaultOptionalIntReturnsEmpty() {
        OptionalInt opt = Defaults.getDefault(OptionalInt.class);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test
    void defaultOptionalLongReturnsEmpty() {
        OptionalLong opt = Defaults.getDefault(OptionalLong.class);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test
    void defaultOptionalDoubleReturnsEmpty() {
        OptionalDouble opt = Defaults.getDefault(OptionalDouble.class);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    @Test
    void defaultObjectReturnsNull() {
        Object obj = Defaults.getDefault(Object.class);
        assertNull(obj);
    }

    @Test
    void defaultBoxedIntegerReturnZero() {
        assertEquals(0, Defaults.getDefault(Integer.class));
    }

    @Test
    void defaultBoxedLongReturnZero() {
        assertEquals(0, Defaults.getDefault(Long.class));
    }

    @Test
    void defaultBoxedDoubleReturnZero() {
        assertEquals(0, Defaults.getDefault(Double.class));
    }
}
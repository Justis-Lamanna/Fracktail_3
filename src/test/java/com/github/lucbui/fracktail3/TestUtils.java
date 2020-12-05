package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class TestUtils {
    public static Method getMethod(Class<?> clazz, String name) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static Field getField(Class<?> clazz, String name) {
        return Arrays.stream(clazz.getFields())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static <T> T assertComponentHasGuard(List<Guard> guards, Class<T> clazz) {
        return guards.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseGet(Assertions::fail);
    }
}

package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import org.junit.jupiter.api.Assertions;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class TestUtils {
    public static Method getMethod(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        return Arrays.stream(clazz.getFields())
                .filter(m -> m.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static Parameter getParameter(Class<?> clazz, String name, String paramName) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name))
                .flatMap(m -> Arrays.stream(m.getParameters()))
                .filter(p -> p.getName().equals(paramName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public static Tuple2<Method, Parameter> getMethodAndParameter(Class<?> clazz, String name, String paramName) {
        Method method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        Parameter param = Arrays.stream(method.getParameters())
                .filter(p -> p.getName().equals(paramName))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return Tuples.of(method, param);
    }

    public static <T> T assertComponentHasGuard(List<Guard> guards, Class<T> clazz) {
        return guards.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .findFirst()
                .orElseGet(Assertions::fail);
    }
}

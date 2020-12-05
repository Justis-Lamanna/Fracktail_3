package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.MethodComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface MethodComponentStrategy {
    MethodComponent decorate(Object obj, Method method, MethodComponent base);

    MethodComponent decorate(Object obj, Field field, MethodComponent base);
}

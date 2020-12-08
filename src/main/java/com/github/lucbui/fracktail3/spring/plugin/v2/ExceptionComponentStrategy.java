package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface ExceptionComponentStrategy {
    ExceptionComponent decorate(Object obj, Method method, ExceptionComponent base);

    ExceptionComponent decorate(Object obj, Field field, ExceptionComponent base);
}

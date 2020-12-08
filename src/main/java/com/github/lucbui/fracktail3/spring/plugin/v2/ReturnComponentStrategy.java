package com.github.lucbui.fracktail3.spring.plugin.v2;

import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public interface ReturnComponentStrategy {
    Optional<ReturnComponent> create(Object obj, Method method);

    ReturnComponent decorate(Object obj, Method method, ReturnComponent base);

    Optional<ReturnComponent> create(Object obj, Field field);

    ReturnComponent decorate(Object obj, Field field, ReturnComponent base);
}

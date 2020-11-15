package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.spring.command.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface CompiledMethodPlugin extends Plugin {
    default Result<ParameterComponent> createParameterComponent(Object obj, Method method, Parameter parameter) {
        return Result.ignore();
    }

    default ParameterComponent decorateParameterComponent(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        return base;
    }

    default int getPluginPriority() {
        return 0;
    }

    default Result<ReturnComponent> createReturnComponent(Object obj, Method method) {
        return Result.ignore();
    }

    default ReturnComponent decorateReturnComponent(Object obj, Method method, ReturnComponent base) {
        return base;
    }
}

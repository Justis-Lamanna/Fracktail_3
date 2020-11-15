package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.spring.command.ParameterComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface CompiledParameterPlugin extends Plugin {
    default ParameterComponent enhanceCompiledParameter(Object obj, Method method, Parameter parameter, ParameterComponent component) {
        return component;
    }

    default Result<ParameterComponent> createCompiledParameter(Object obj, Method method, Parameter parameter) {
        return Result.ignore();
    }

    default int getCPPPriority() {
        return 0;
    }
}

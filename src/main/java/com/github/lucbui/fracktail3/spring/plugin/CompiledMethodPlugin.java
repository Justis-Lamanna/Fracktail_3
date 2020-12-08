package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Deprecated
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

    default Result<ReturnComponent> createFieldReturnComponent(Object obj, Field field) {
        return Result.ignore();
    }

    default ReturnComponent decorateFieldReturnComponent(Object obj, Field field, ReturnComponent base) {
        return base;
    }

    default MethodComponent decorateMethodComponent(Object obj, Method method, MethodComponent base) {
        return base;
    }

    default MethodComponent decorateFieldComponent(Object obj, Field field, MethodComponent base) {
        return base;
    }

    default ExceptionComponent decorateExceptionComponent(Object obj, Method method, ExceptionComponent base) { return base; }

    default ExceptionComponent decorateFieldExceptionComponent(Object obj, Field field, ExceptionComponent base) { return base; }
}

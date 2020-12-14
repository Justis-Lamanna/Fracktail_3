package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;

import java.util.function.Function;

public abstract class ParameterBaseComponent<T extends BaseContext<?>> {
    final ParameterConverterFunction<? super T> func;

    public ParameterBaseComponent(ParameterConverterFunction<T> func) {
        this.func = func;
    }

    public ParameterConverterFunction<? super T> getFunc() {
        return func;
    }

    public interface ParameterConverterFunction<T extends BaseContext<?>> extends Function<T, Object> {}
}

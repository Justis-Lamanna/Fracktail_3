package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;

import java.util.function.Function;

/**
 * A base component which resolves the context into a parameter to be injected into the method
 */
public abstract class ParameterBaseComponent<T extends BaseContext<?>> {
    final ParameterConverterFunction<? super T> func;

    /**
     * Initialize this component with a function
     * @param func The function to use
     */
    public ParameterBaseComponent(ParameterConverterFunction<T> func) {
        this.func = func;
    }

    /**
     * Get the ParameterConverterFunction being used
     * @return The function used
     */
    public ParameterConverterFunction<? super T> getFunc() {
        return func;
    }

    /**
     * A function which converts the CommandUseContext into some injectable parameter
     */
    public interface ParameterConverterFunction<T extends BaseContext<?>> extends Function<T, Object> {}
}

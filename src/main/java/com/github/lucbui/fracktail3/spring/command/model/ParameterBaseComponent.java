package com.github.lucbui.fracktail3.spring.command.model;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.function.Function;

/**
 * A base component which resolves the context into a parameter to be injected into the method
 */
@Getter
public abstract class ParameterBaseComponent<T> {
    protected final TypeDescriptor type;
    protected final ParameterConverterFunction<? super T> func;

    /**
     * Initialize this component with a function
     * @param type
     * @param func The function to use
     */
    public ParameterBaseComponent(TypeDescriptor type, ParameterConverterFunction<? super T> func) {
        this.type = type;
        this.func = func;
    }

    /**
     * A function which converts the CommandUseContext into some injectable parameter
     */
    public interface ParameterConverterFunction<T> extends Function<T, Object> {}
}

package com.github.lucbui.fracktail3.spring.command.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.function.Function;

/**
 * A base component which resolves the context into a parameter to be injected into the method
 */
@Getter
@Setter
public abstract class ParameterBaseComponent<T> {
    protected final TypeDescriptor type;
    protected ParameterConverterFunction<? super T> func;

    /**
     * Initialize this component with a function
     * @param type The parameter's type
     */
    public ParameterBaseComponent(TypeDescriptor type) {
        this.type = type;
    }

    /**
     * A function which converts the CommandUseContext into some injectable parameter
     */
    public interface ParameterConverterFunction<T> extends Function<T, Object> {}
}

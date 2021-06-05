package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for further limiting of only specific values of a given type
 * @param <T> The type being considered
 */
@Getter
public class SubsetLimit<T> extends ClassLimit {
    private final List<T> acceptableValues;

    /**
     * Initialize this limit
     * @param type The type of permissible objects
     * @param acceptableValues A list of acceptable values
     */
    public SubsetLimit(TypeDescriptor type, List<T> acceptableValues) {
        super(type);
        this.acceptableValues = new ArrayList<>(acceptableValues);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) && acceptableValues.contains(obj);
    }

    @Override
    public Object getDefault() {
        return isOptional() ? super.getDefault() : acceptableValues.get(0);
    }
}

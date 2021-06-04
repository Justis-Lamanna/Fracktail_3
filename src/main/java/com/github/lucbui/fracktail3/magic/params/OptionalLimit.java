package com.github.lucbui.fracktail3.magic.params;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class OptionalLimit implements TypeLimits {
    @JsonUnwrapped
    private final TypeLimits wrapped;

    @Override
    public boolean matches(Object obj) {
        return obj == null || wrapped.matches(obj);
    }

    @Override
    public TypeLimits optional(boolean opt) {
        if(opt) {
            return this;
        } else {
            return wrapped;
        }
    }
}

package com.github.lucbui.fracktail3.magic.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A basic TypeLimit which enforces that an object must be a specific type
 */
@Data
public class ClassLimit implements TypeLimits {
    /**
     * The descriptor of this type.
     */
    private final TypeDescriptor type;

    public ClassLimit(Class<?> clazz) {
        this.type = TypeDescriptor.valueOf(clazz);
    }

    public ClassLimit(TypeDescriptor type) {
        this.type = type;
    }

    @JsonProperty("type")
    public Class<?> getType() {
        return type.getType();
    }

    @JsonIgnore
    public TypeDescriptor getTypeDescriptor() {
        return type;
    }

    @Override
    public boolean matches(Object obj) {
        TypeDescriptor objType = TypeDescriptor.forObject(obj);
        return objType != null && objType.isAssignableTo(type);
    }
}

package com.github.milomarten.fracktail3.magic.params;

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
    private boolean optional;

    public ClassLimit(Class<?> clazz) {
        this(TypeDescriptor.valueOf(clazz));
    }

    public ClassLimit(TypeDescriptor type) {
        this(type, false);
    }

    public ClassLimit(Class<?> clazz, boolean optional) {
        this(TypeDescriptor.valueOf(clazz), optional);
    }

    public ClassLimit(TypeDescriptor type, boolean optional) {
        this.type = type;
        this.optional = optional;
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
        if(objType == null && optional) return true;
        return objType != null && objType.isAssignableTo(type);
    }

    @Override
    public TypeLimits optional(boolean opt) {
        this.optional = opt;
        return this;
    }
}

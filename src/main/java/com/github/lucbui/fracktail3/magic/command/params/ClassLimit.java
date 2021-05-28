package com.github.lucbui.fracktail3.magic.command.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.convert.TypeDescriptor;

@Data
public class ClassLimit implements TypeLimits {
    private final TypeDescriptor type;

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

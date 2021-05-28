package com.github.lucbui.fracktail3.magic.command.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.core.convert.TypeDescriptor;

@Data
public class ClassLimit implements TypeLimits {
    @JsonIgnore
    private final TypeDescriptor type;

    @JsonProperty("type")
    public Class<?> getType() {
        return type.getType();
    }

    @Override
    public boolean matches(Object obj) {
        TypeDescriptor objType = TypeDescriptor.forObject(obj);
        return objType != null && objType.isAssignableTo(type);
    }
}

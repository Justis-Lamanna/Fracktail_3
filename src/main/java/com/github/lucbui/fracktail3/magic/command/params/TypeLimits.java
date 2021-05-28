package com.github.lucbui.fracktail3.magic.command.params;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface TypeLimits {
    boolean matches(Object obj);
}

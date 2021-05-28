package com.github.lucbui.fracktail3.magic.params;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A platform-agnostic way to assert a field's value must match certain constraints
 * All subclasses of this field should be completely serializable and easily converted
 * to and from JSON. This allows for UIs to adjust their inputs accordingly to allow for
 * client-side validation
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface TypeLimits {
    boolean matches(Object obj);
}

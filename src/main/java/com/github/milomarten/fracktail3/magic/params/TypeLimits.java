package com.github.milomarten.fracktail3.magic.params;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.milomarten.fracktail3.spring.service.Defaults;

/**
 * A platform-agnostic way to assert a field's value must match certain constraints
 * All subclasses of this field should be completely serializable and easily converted
 * to and from JSON. This allows for UIs to adjust their inputs accordingly to allow for
 * client-side validation
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface TypeLimits {
    boolean matches(Object obj);

    /**
     * Get the default value for this type
     * @return The default value
     */
    default Object getDefault() {
        return Defaults.getDefault(getClass());
    }

    default TypeLimits optional(boolean opt) {
        if(opt) {
            return new OptionalLimit(this);
        } else {
            return this;
        }
    }
}

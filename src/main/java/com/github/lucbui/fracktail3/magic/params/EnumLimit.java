package com.github.lucbui.fracktail3.magic.params;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Further refining of a SubsetLimit which acts on an Enum set
 * @param <T> The enum type
 */
public class EnumLimit<T extends Enum<T>> extends SubsetLimit<T> {
    /**
     * Initialize this limit
     *
     * @param type             The type of permissible objects
     */
    public EnumLimit(TypeDescriptor type) {
        super(type, EnumUtils.getEnumList((Class<T>)type.getObjectType()));
    }
}

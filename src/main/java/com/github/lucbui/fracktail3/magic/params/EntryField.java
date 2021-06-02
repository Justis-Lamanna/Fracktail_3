package com.github.lucbui.fracktail3.magic.params;

import lombok.Builder;
import lombok.Data;

/**
 * Attempts to represent a legal create/edit field that can be represented on a variety of platforms
 */
@Data
@Builder
public class EntryField {
    /**
     * The ID of the field
     */
    private final String id;
    /**
     * A human-readable field name
     */
    private final String name;
    /**
     * A human-readable field description
     */
    private final String description;
    /**
     * The type of this field, and any limits to it.
     */
    @Builder.Default private final TypeLimits typeLimit = AnyType.INSTANCE;

    /**
     * Whether this field is optional or not
     */
    @Builder.Default private final boolean optional = false;
}

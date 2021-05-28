package com.github.lucbui.fracktail3.magic.params;

import lombok.Data;

/**
 * Attempts to represent a legal create/edit field that can be represented on a variety of platforms
 */
@Data
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
    private final TypeLimits typeLimit;
}

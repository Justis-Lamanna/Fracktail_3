package com.github.lucbui.fracktail3.magic.platform.formatting;

import lombok.Data;

/**
 * Describes how a platform should format a certain intent
 */
@Data
public class Formatting {
    public static final Formatting NONE = new Formatting("", "");

    private final String prefix;
    private final String suffix;

    /**
     * Describes a formatting that wraps its internal text with the same prefix and suffix
     * @param wrappingStr The String to wrap around the input
     * @return The created Formatting
     */
    public static Formatting wrapped(String wrappingStr) {
        return new Formatting(wrappingStr, wrappingStr);
    }
}

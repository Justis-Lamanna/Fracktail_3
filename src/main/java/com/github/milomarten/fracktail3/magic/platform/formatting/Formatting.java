package com.github.milomarten.fracktail3.magic.platform.formatting;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Function;

/**
 * Describes how a platform should format a certain intent
 */
@Data
@AllArgsConstructor
public class Formatting {
    public static final Formatting NONE = new Formatting("", "");

    private final String prefix;
    private final String suffix;
    private final Function<String, String> messageTransformer;

    public Formatting(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.messageTransformer = Function.identity();
    }

    /**
     * Describes a formatting that wraps its internal text with the same prefix and suffix
     * @param wrappingStr The String to wrap around the input
     * @return The created Formatting
     */
    public static Formatting wrapped(String wrappingStr) {
        return new Formatting(wrappingStr, wrappingStr);
    }

    public static Formatting transforming(Function<String, String> transformer) {
        return new Formatting("", "", transformer);
    }
}

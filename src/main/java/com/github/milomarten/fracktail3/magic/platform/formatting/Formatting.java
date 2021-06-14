package com.github.milomarten.fracktail3.magic.platform.formatting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * Describes how a platform should format a certain intent
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Formatting {
    public static final Formatting NONE = new Formatting(Function.identity());

    private final Function<String, String> messageTransformer;

    /**
     * Format an input string according to the rules
     * @param message The message to format
     * @return The formatted message
     */
    public String format(String message) {
        return messageTransformer.apply(message);
    }

    /**
     * Describes a formatting that wraps its internal text with the same prefix and suffix
     * @param wrappingStr The String to wrap around the input
     * @return The created Formatting
     */
    public static Formatting wrapped(String wrappingStr) {
        return new Formatting(str -> StringUtils.wrap(str, wrappingStr));
    }

    /**
     * Describe a formatting which does custom String modification of its insides
     * @param transformer The transformer to use
     * @return The created Formatting
     */
    public static Formatting transforming(Function<String, String> transformer) {
        return new Formatting(transformer);
    }

    /**
     * Describe a formatting which prefixes the message with a string
     * @param prefixStr The string to prefix with
     * @return The created Formatting
     */
    public static Formatting prefixed(String prefixStr) {
        return new Formatting(str -> prefixStr + str);
    }

    /**
     * Describe a formatting which prefixes and suffixes the message with seperate strings
     * @param prefixStr The string to prefix
     * @param suffixStr The string to suffix
     * @return The created Formatting
     */
    public static Formatting wrapped(String prefixStr, String suffixStr) {
        return new Formatting(str -> prefixStr + str + suffixStr);
    }

    /**
     * Wraps the message with XML-style tags
     * message -> &gt;tag&lt;message&gt;/tag&lt;
     * @param tag The tag to use
     * @return The created Formatting
     */
    public static Formatting xml(String tag) {
        return new Formatting(str -> "<" + tag + ">" + str + "</" + tag + ">");
    }
}

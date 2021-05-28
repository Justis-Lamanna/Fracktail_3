package com.github.lucbui.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Allows for further refining of a String via an enforced size
 */
@Getter
public class StringLengthLimit extends ClassLimit {
    private final int minLength;
    private final int maxLength;

    private StringLengthLimit(int minLength, int maxLength) {
        super(TypeDescriptor.valueOf(String.class));
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
     * Enforce that a string must be minLength or larger in size
     * @param minLength The minimum permissible length
     * @return The created limit
     */
    public static StringLengthLimit atLeast(int minLength) {
        return new StringLengthLimit(minLength, -1);
    }

    /**
     * Enforces that a string must be at maxLength or smaller in size
     * @param maxLength The maximum permissible length
     * @return The created limit
     */
    public static StringLengthLimit atMost(int maxLength) {
        return new StringLengthLimit(0, maxLength);
    }

    /**
     * Enforces that a string must at least minLength, and at most maxLength in size
     * @param minLength The minimum string length
     * @param maxLength The maximum string length
     * @return The created limit
     */
    public static StringLengthLimit between(int minLength, int maxLength) {
        if(minLength > maxLength) {
            throw new IllegalArgumentException(minLength + " > " + maxLength);
        }
        return new StringLengthLimit(minLength, maxLength);
    }

    /**
     * Enforces that a string must be exactly length in size
     * @param length The length the string must be
     * @return The created limit
     */
    public static StringLengthLimit exactly(int length) {
        return new StringLengthLimit(length, length);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                (minLength == 0 || ((String)obj).length() >= minLength) &&
                (maxLength == -1 || ((String)obj).length() <= maxLength);
    }
}

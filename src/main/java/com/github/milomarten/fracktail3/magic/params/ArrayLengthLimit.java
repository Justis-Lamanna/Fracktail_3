package com.github.milomarten.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Allows for further refining of a String via an enforced size
 */
@Getter
public class ArrayLengthLimit extends LengthLimit {
    private ArrayLengthLimit(int minLength, int maxLength) {
        super(TypeDescriptor.valueOf(Object[].class), minLength, maxLength);
    }

    /**
     * Enforce that a string must be minLength or larger in size
     * @param minLength The minimum permissible length
     * @return The created limit
     */
    public static ArrayLengthLimit atLeast(int minLength) {
        return new ArrayLengthLimit(minLength, -1);
    }

    /**
     * Enforces that a string must be at maxLength or smaller in size
     * @param maxLength The maximum permissible length
     * @return The created limit
     */
    public static ArrayLengthLimit atMost(int maxLength) {
        return new ArrayLengthLimit(0, maxLength);
    }

    /**
     * Enforces that a string must at least minLength, and at most maxLength in size
     * @param minLength The minimum string length
     * @param maxLength The maximum string length
     * @return The created limit
     */
    public static ArrayLengthLimit between(int minLength, int maxLength) {
        if(minLength > maxLength) {
            throw new IllegalArgumentException(minLength + " > " + maxLength);
        }
        return new ArrayLengthLimit(minLength, maxLength);
    }

    /**
     * Enforces that a string must be exactly length in size
     * @param length The length the string must be
     * @return The created limit
     */
    public static ArrayLengthLimit exactly(int length) {
        return new ArrayLengthLimit(length, length);
    }


    @Override
    public int length(Object obj) {
        return ((Object[])obj).length;
    }
}

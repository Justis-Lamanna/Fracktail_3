package com.github.milomarten.fracktail3.magic.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Map;

/**
 * Allows for further refining of a String via an enforced size
 */
@Getter
public class MapLengthLimit extends LengthLimit {
    private MapLengthLimit(int minLength, int maxLength) {
        super(TypeDescriptor.valueOf(Map.class), minLength, maxLength);
    }

    /**
     * Enforce that a string must be minLength or larger in size
     * @param minLength The minimum permissible length
     * @return The created limit
     */
    public static MapLengthLimit atLeast(int minLength) {
        return new MapLengthLimit(minLength, -1);
    }

    /**
     * Enforces that a string must be at maxLength or smaller in size
     * @param maxLength The maximum permissible length
     * @return The created limit
     */
    public static MapLengthLimit atMost(int maxLength) {
        return new MapLengthLimit(0, maxLength);
    }

    /**
     * Enforces that a string must at least minLength, and at most maxLength in size
     * @param minLength The minimum string length
     * @param maxLength The maximum string length
     * @return The created limit
     */
    public static MapLengthLimit between(int minLength, int maxLength) {
        if(minLength > maxLength) {
            throw new IllegalArgumentException(minLength + " > " + maxLength);
        }
        return new MapLengthLimit(minLength, maxLength);
    }

    /**
     * Enforces that a string must be exactly length in size
     * @param length The length the string must be
     * @return The created limit
     */
    public static MapLengthLimit exactly(int length) {
        return new MapLengthLimit(length, length);
    }


    @Override
    public int length(Object obj) {
        return ((Map<?, ?>)obj).size();
    }
}

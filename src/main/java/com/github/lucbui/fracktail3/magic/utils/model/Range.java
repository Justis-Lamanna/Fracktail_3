package com.github.lucbui.fracktail3.magic.utils.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.OptionalInt;

/**
 * Encapsulates a (potentially-unbounded) range of values, starting at 0
 */
public class Range {
    private final int start;
    private final int end;

    /**
     * Create a range between start and end
     * @param start The start value (inclusive)
     * @param end The end value (inclusive)
     */
    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Create a completely unbounded range
     * @return A range from 0 to infinity
     */
    public static Range unbounded() {
        return new Range(0, -1);
    }

    /**
     * A "range" that is a single value
     * @param value The value
     * @return A range containing only the provided value
     */
    public static Range single(int value) {
        return new Range(value, value);
    }

    /**
     * A range from start to end, inclusive
     * @param start The start value
     * @param end The end value
     * @return A range from start to end
     */
    public static Range fromTo(int start, int end) {
        return new Range(start, end);
    }

    /**
     * A range from start, to infinity
     * @param start The start value
     * @return A range from start to infinity
     */
    public static Range fromOnward(int start) {
        return new Range(start, -1);
    }

    /**
     * A range from 0 to end
     * @param end The end value
     * @return A range from 0 to end
     */
    public static Range to(int end) {
        return new Range(0, end);
    }

    /**
     * Get the starting value
     * @return The starting value
     */
    public int getStart() {
        return start;
    }

    /**
     * Get the ending value, if applicable
     * @return The end value, or an empty optional if unbounded
     */
    public OptionalInt getEnd() {
        return isUnbounded() ? OptionalInt.empty() : OptionalInt.of(end);
    }

    /**
     * Test if this Range is unbounded
     * @return True, if the range is unbounded
     */
    @JsonIgnore
    public boolean isUnbounded() {
        return end == -1;
    }

    /**
     * Test if a value is in the range
     * @param value The value to test
     * @return True, if the value is inside this range
     */
    public boolean isInside(int value) {
        OptionalInt endpoint = getEnd();
        if(endpoint.isPresent()) {
            return value >= start && endpoint.getAsInt() <= end;
        } else {
            return value >= start;
        }
    }

    @Override
    public String toString() {
        if(isUnbounded()) {
            return "Range{" + this.start + "->}";
        } else {
            return "Range{" + this.start + "->" + this.end + "}";
        }
    }
}

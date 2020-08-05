package com.github.lucbui.fracktail3.magic.handlers;

public class Range {
    private final int start;
    private final int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static Range unbounded() {
        return new Range(0, -1);
    }

    public static Range single(int value) {
        return new Range(value, value);
    }

    public static Range fromTo(int start, int end) {
        return new Range(start, end);
    }

    public static Range fromOnward(int start) {
        return new Range(start, -1);
    }

    public static Range to(int end) {
        return new Range(0, end);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isUnbounded() {
        return end == -1;
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

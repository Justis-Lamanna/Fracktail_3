package com.github.lucbui.fracktail3.magic.command.params;

import lombok.Getter;
import org.springframework.core.convert.TypeDescriptor;

@Getter
public class StringLengthLimit extends ClassLimit {
    private final int minLength;
    private final int maxLength;

    private StringLengthLimit(int minLength, int maxLength) {
        super(TypeDescriptor.valueOf(String.class));
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public static StringLengthLimit atLeast(int minLength) {
        return new StringLengthLimit(minLength, -1);
    }

    public static StringLengthLimit atMost(int maxLength) {
        return new StringLengthLimit(0, maxLength);
    }

    public static StringLengthLimit between(int minLength, int maxLength) {
        return new StringLengthLimit(minLength, maxLength);
    }

    @Override
    public boolean matches(Object obj) {
        return super.matches(obj) &&
                (minLength == 0 || ((String)obj).length() >= minLength) &&
                (maxLength == -1 || ((String)obj).length() <= maxLength);
    }
}

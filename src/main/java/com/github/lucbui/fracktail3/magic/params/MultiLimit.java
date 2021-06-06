package com.github.lucbui.fracktail3.magic.params;

import lombok.Data;

import java.util.Arrays;

@Data
public class MultiLimit implements TypeLimits {
    private final TypeLimits[] nested;

    @Override
    public boolean matches(Object obj) {
        return Arrays.stream(nested)
                .allMatch(l -> l.matches(obj));
    }
}

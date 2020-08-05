package com.github.lucbui.fracktail3.magic.handlers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class NamedParameters {
    public static final NamedParameters EMPTY = new NamedParameters(Collections.emptyMap());

    private final Map<String, String> params;

    public NamedParameters(Map<String, String> params) {
        this.params = Collections.unmodifiableMap(params);
    }

    public boolean hasKey(String key) {
        return params.containsKey(key);
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(params.get(key));
    }
}

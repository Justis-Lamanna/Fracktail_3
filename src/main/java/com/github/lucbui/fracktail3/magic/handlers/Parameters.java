package com.github.lucbui.fracktail3.magic.handlers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Parameters {
    private final Map<String, String> params;

    public Parameters(Map<String, String> params) {
        this.params = Collections.unmodifiableMap(params);
    }

    public boolean hasKey(String key) {
        return params.containsKey(key);
    }

    public Optional<String> getValue(String key) {
        return Optional.ofNullable(params.get(key));
    }
}

package com.github.lucbui.fracktail3.magic.platform.context;

import java.util.Optional;

public class Parameters {
    private final String raw;
    private final String[] parsed;

    public Parameters(String raw, String[] parsed) {
        this.raw = raw;
        this.parsed = parsed;
    }

    public String getRaw() {
        return raw;
    }

    public String[] getParsed() {
        return parsed;
    }

    public Optional<String> getParameter(int idx) {
        if(idx >= parsed.length) {
            return Optional.empty();
        } else {
            return Optional.of(parsed[idx]);
        }
    }

    public int getNumberOfParameters() {
        return parsed.length;
    }
}

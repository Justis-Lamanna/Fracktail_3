package com.github.lucbui.fracktail3.magic.platform.context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Optional<String>> getParameters(int start, int end) {
        if(parsed.length == 0) {
            return Collections.emptyList();
        } else if(end < 0) {
            return Arrays.stream(parsed, start, parsed.length + end + 1)
                    .map(Optional::of)
                    .collect(Collectors.toList());
        } else {
            return IntStream.rangeClosed(start, end)
                .mapToObj(i -> i < parsed.length ? Optional.of(parsed[i]) : Optional.<String>empty())
                .collect(Collectors.toList());
        }
    }

    public int getNumberOfParameters() {
        return parsed.length;
    }
}

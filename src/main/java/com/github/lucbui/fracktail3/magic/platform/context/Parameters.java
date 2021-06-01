package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.command.Command;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parameters {
    private final String raw;
    private final Member[] parsed;

    public Parameters(String raw, Member[] parsed) {
        this.raw = raw;
        this.parsed = parsed;
    }

    public String getRaw() {
        return raw;
    }

    public Object[] getParsed() {
        return Arrays.stream(parsed).map(Member::getValue).toArray();
    }

    public <T> Optional<T> getParameter(int idx) {
        if(idx >= parsed.length) {
            return Optional.empty();
        } else {
            return Optional.ofNullable((T)parsed[idx]);
        }
    }

    public <T> List<Optional<T>> getParameters(int start, int end) {
        if(parsed.length == 0) {
            return Collections.emptyList();
        } else if(end < 0) {
            return Arrays.stream(parsed, start, parsed.length + end + 1)
                    .map(o -> Optional.ofNullable((T)o))
                    .collect(Collectors.toList());
        } else {
            return IntStream.rangeClosed(start, end)
                .mapToObj(i -> i < parsed.length ? Optional.of((T)parsed[i]) : Optional.<T>empty())
                .collect(Collectors.toList());
        }
    }

    public int getNumberOfParameters() {
        return parsed.length;
    }

    @Data
    public static class Member {
        private final Command.Parameter parameter;
        private final Object value;
    }
}

package com.github.lucbui.fracktail3.spring.plugin;

public class Result<T> {
    final boolean ignored;
    final T result;

    public Result(boolean ignored, T result) {
        this.ignored = ignored;
        this.result = result;
    }

    public static <T> Result<T> value(T value) {
        return new Result<>(true, value);
    }

    public static <T> Result<T> ignore() {
        return new Result<>(false, null);
    }

    public boolean isIgnored() {
        return ignored;
    }

    public T getResult() {
        if(!isIgnored()) {
            throw new IllegalArgumentException("Result was unsupported");
        }
        return result;
    }

    public T or(T defaultValue) {
        return isIgnored() ? result : defaultValue;
    }

    public boolean isResult() {
        return !ignored;
    }
}

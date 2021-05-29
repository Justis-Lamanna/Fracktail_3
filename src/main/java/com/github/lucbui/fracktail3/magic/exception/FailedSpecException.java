package com.github.lucbui.fracktail3.magic.exception;

public class FailedSpecException extends RuntimeException {
    public FailedSpecException() {
    }

    public FailedSpecException(String message) {
        super(message);
    }

    public FailedSpecException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedSpecException(Throwable cause) {
        super(cause);
    }
}

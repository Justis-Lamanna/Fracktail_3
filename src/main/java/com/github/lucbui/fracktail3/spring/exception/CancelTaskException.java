package com.github.lucbui.fracktail3.spring.exception;

/**
 * When thrown in a @Schedule-annotated method, future runs of the task are cancelled.
 */
public class CancelTaskException extends RuntimeException {
    public CancelTaskException() {
    }

    public CancelTaskException(String message) {
        super(message);
    }

    public CancelTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public CancelTaskException(Throwable cause) {
        super(cause);
    }
}

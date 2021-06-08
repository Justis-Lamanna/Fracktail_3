package com.github.milomarten.fracktail3.spring.schedule.exception;

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

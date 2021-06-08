package com.github.milomarten.fracktail3.magic.exception;

/**
 * An exception which indicates ResourceBundle resolution failed.
 */
public class ResolveFailedException extends RuntimeException {
    public ResolveFailedException() {
    }

    public ResolveFailedException(String message) {
        super(message);
    }

    public ResolveFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolveFailedException(Throwable cause) {
        super(cause);
    }
}

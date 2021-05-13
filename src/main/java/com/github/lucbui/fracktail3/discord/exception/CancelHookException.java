package com.github.lucbui.fracktail3.discord.exception;

public class CancelHookException extends RuntimeException {
    public CancelHookException() {
    }

    public CancelHookException(String message) {
        super(message);
    }

    public CancelHookException(String message, Throwable cause) {
        super(message, cause);
    }

    public CancelHookException(Throwable cause) {
        super(cause);
    }
}

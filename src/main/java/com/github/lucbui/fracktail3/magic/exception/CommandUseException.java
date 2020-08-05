package com.github.lucbui.fracktail3.magic.exception;

public class CommandUseException extends RuntimeException {
    public CommandUseException() {
    }

    public CommandUseException(String message) {
        super(message);
    }

    public CommandUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandUseException(Throwable cause) {
        super(cause);
    }
}

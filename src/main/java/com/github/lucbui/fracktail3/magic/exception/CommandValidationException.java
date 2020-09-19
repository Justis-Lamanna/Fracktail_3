package com.github.lucbui.fracktail3.magic.exception;

/**
 * Exception indicating validation of a command's parameter failed.
 * Unlike other exceptions, this type (and subclasses) will be caught by the Bot engine, and will respond with
 * the indicated message.
 */
public class CommandValidationException extends RuntimeException {
    private final String message;

    /**
     * Generate an exception which responds with a literal message.
     * @param message The message to respond with.
     * @return A CommandValidationException which returns the supplied message.
     */
    public CommandValidationException(String message) {
        this.message = message;
    }

    /**
     * Resolves and returns the message
     * @return The resolved message
     */
    public String getMessage() {
        return message;
    }
}

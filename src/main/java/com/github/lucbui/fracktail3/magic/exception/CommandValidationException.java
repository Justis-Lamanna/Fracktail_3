package com.github.lucbui.fracktail3.magic.exception;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * Exception indicating validation of a command's parameter failed.
 * Unlike other exceptions, this type (and subclasses) will be caught by the Bot engine, and will respond with
 * the indicated message.
 */
public class CommandValidationException extends RuntimeException {
    private final FormattedString message;

    /**
     * Generate an exception which responds with a potentially formatted message
     * @param message The message to send
     */
    public CommandValidationException(FormattedString message) {
        this.message = message;
    }

    /**
     * Generate an exception which responds with a literal message.
     * @param message The message to respond with.
     */
    public CommandValidationException(String message) {
        this(FormattedString.from(message));
    }

    /**
     * Resolves and returns the message
     * @return The resolved message
     */
    public String getMessage() {
        return message.getRaw();
    }

    /**
     * Format the message as appropriate
     * @param ctx The context to use
     * @return Asynchronous formatted message
     */
    public Mono<String> getFormattedMessage(BaseContext<?> ctx) {
        return message.getFor(ctx);
    }
}

package com.github.lucbui.fracktail3.magic.exception;

import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatters;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Exception indicating validation of a command's parameter failed.
 * Unlike other exceptions, this type (and subclasses) will be caught by the Bot engine, and will respond with
 * the indicated message.
 */
public class CommandValidationException extends RuntimeException {
    private final String message;
    private final ContextFormatter formatter;

    /**
     * Generate an exception which responds with a potentially formatted message
     * @param message The message to send
     * @param formatter The formatter to use when parsing the message
     */
    public CommandValidationException(String message, ContextFormatter formatter) {
        this.message = message;
        this.formatter = formatter;
    }

    /**
     * Generate an exception which responds with a literal message.
     * @param message The message to respond with.
     */
    public CommandValidationException(String message) {
        this(message, ContextFormatters.getDefault());
    }

    /**
     * Resolves and returns the message
     * @return The resolved message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the formatter to use with the message
     * @return The formatter
     */
    public ContextFormatter getFormatter() {
        return formatter;
    }

    /**
     * Format the message as appropriate
     * @param ctx The context to use
     * @return Asynchronous formatted message
     */
    public Mono<String> getFormattedMessage(CommandContext ctx) {
        return getFormatter().format(getMessage(), ctx);
    }
}

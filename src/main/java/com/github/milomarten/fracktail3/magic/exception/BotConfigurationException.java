package com.github.milomarten.fracktail3.magic.exception;

/**
 * Exception indicating the bot was incorrectly configured
 */
public class BotConfigurationException extends RuntimeException {
    public BotConfigurationException() {
    }

    public BotConfigurationException(String message) {
        super(message);
    }

    public BotConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BotConfigurationException(Throwable cause) {
        super(cause);
    }
}

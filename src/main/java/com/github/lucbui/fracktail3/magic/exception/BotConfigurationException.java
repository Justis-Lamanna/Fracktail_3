package com.github.lucbui.fracktail3.magic.exception;

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

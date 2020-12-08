package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;

/**
 * Indicates an object can be treated as a response by the bot, if returned via a @Command-annotated method or field
 */
public interface BotResponse {
    /**
     * Construct a FormattedString that represents this object
     * @return a FormattedString to respond with
     */
    FormattedString respondWith();
}

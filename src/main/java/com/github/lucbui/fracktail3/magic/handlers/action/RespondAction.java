package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Action which responds to the user of the command in some way
 */
public class RespondAction implements Action {
    private final ContextFormatter formatter;
    private final String msg;

    /**
     * Initialize the action
     * @param msg The message to respond with
     */
    public RespondAction(String msg) {
        this(msg, ContextFormatter.DEFAULT);
    }

    /**
     * Initialize the action
     * @param msg The message to respond with
     * @param formatter Dictates how msg should be formatted
     */
    public RespondAction(String msg, ContextFormatter formatter) {
        this.formatter = Objects.requireNonNull(formatter);
        this.msg = Objects.requireNonNull(msg);
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return formatter.format(msg, context)
                .flatMap(context::respond)
                .then();
    }
}

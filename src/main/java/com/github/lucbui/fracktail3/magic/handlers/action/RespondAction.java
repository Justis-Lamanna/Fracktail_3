package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Action which responds to the user of the command in some way
 */
public class RespondAction implements Action {
    private final String msg;

    /**
     * Initialize the action
     * @param msg The message to respond with
     */
    public RespondAction(String msg) {
        this.msg = msg;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return context.respond(msg).then();
    }
}

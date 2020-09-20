package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

/**
 * Action which responds to the user of the command in some way
 */
public class RespondAction extends SendMessageAction {
    /**
     * Initialize the action
     * @param msg The message to respond with
     */
    public RespondAction(String msg) {
        super(msg);
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.respondLocalized(message);
    }
}

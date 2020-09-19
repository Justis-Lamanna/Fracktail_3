package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public class RespondAction extends SendMessageAction {
    public RespondAction(String msg) {
        super(msg);
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.respond(message);
    }
}

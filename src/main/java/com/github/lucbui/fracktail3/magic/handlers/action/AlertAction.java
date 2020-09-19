package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public class AlertAction extends SendMessageAction {
    public AlertAction(String msg) {
        super(msg);
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.alert(message);
    }
}

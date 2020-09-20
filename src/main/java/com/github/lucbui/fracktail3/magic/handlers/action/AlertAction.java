package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * An action which DMs the owner if something needs their attention
 */
public class AlertAction extends SendMessageAction {
    /**
     * Initialize alert message
     * @param msg The alert message
     */
    public AlertAction(String msg) {
        super(msg);
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.alert(message);
    }
}

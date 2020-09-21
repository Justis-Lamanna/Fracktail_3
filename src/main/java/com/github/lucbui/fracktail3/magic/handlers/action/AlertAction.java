package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

/**
 * An action which DMs the owner if something needs their attention
 */
public class AlertAction implements Action {
    private final String msg;

    /**
     * Initialize the action
     * @param msg The message to respond with
     */
    public AlertAction(String msg) {
        this.msg = msg;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return context.alert(msg).then();
    }
}

package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * An action which DMs the owner if something needs their attention
 */
public class AlertAction implements Action {
    private final FormattedString msg;

    /**
     * Initialize the action
     * @param msg The message to respond with
     */
    public AlertAction(FormattedString msg) {
        this.msg = Objects.requireNonNull(msg);
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return msg.getFor(context).flatMap(context::respond).then();
    }
}

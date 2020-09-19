package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

public abstract class SendMessageAction implements Action {
    private final String msg;

    public SendMessageAction(String msg) {
        this.msg = msg;
    }

    public String getResolver() {
        return msg;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        MessageFormat format = new MessageFormat(msg, context.getLocale());
        return context.getExtendedVariableMap()
                .map(format::format)
                .flatMap(str -> sendMessage(context, str))
                .then();
    }

    protected abstract Mono<?> sendMessage(CommandContext context, String message);
}

package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.ibm.icu.text.MessageFormat;
import reactor.core.publisher.Mono;

/**
 * Abstract class which sends some sort of message
 * Input message will be formatted, using the context's locale,
 * and with variables injected.
 */
public abstract class SendMessageAction implements Action {
    private final String msg;

    /**
     * Initialize action
     * @param msg The message to send
     */
    public SendMessageAction(String msg) {
        this.msg = msg;
    }

    /**
     * Get the message being sent
     * @return The message being sent
     */
    public String getMessage() {
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

    /**
     * Perform the action of sending the message
     * @param context The context of the message
     * @param message The messages to send
     * @return Any asynchronous object to indicate completion
     */
    protected abstract Mono<?> sendMessage(CommandContext context, String message);
}

package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

public class AlertAction extends SendMessageAction {
    public AlertAction(Resolver<String> resolver) {
        super(resolver);
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.alert(message);
    }
}

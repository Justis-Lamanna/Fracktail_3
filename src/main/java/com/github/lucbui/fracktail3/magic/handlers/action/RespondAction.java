package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

public class RespondAction extends SendMessageAction {
    public RespondAction(Resolver<String> resolver) {
        super(resolver);
    }

    public static RespondAction literal(String response) {
        return new RespondAction(Resolver.identity(response));
    }

    public static RespondAction i18n(String response) {
        return new RespondAction(new I18NResolver(response));
    }

    public static RespondAction i18n(String response, String _default) {
        return new RespondAction(new I18NResolver(response, _default));
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.respond(message);
    }
}

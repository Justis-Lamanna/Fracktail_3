package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import reactor.core.publisher.Mono;

public class AlertAction extends SendMessageAction {
    public AlertAction(Resolver<String> resolver) {
        super(resolver);
    }

    public static AlertAction literal(String response) {
        return new AlertAction(Resolver.identity(response));
    }

    public static AlertAction i18n(String response) {
        return new AlertAction(new I18NResolver(response));
    }

    public static AlertAction i18n(String response, String _default) {
        return new AlertAction(new I18NResolver(response, _default));
    }

    @Override
    protected Mono<?> sendMessage(CommandContext context, String message) {
        return context.alert(message);
    }
}

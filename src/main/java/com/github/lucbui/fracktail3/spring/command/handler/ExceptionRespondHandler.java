package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ExceptionRespondHandler implements ExceptionComponent.ExceptionHandler {
    private final FormattedString fString;

    public ExceptionRespondHandler(FormattedString fString) {
        this.fString = fString;
    }

    public FormattedString getfString() {
        return fString;
    }

    @Override
    public Mono<Void> apply(CommandUseContext<?> context, Throwable throwable) {
        return context.respond(fString, Collections.singletonMap("message", throwable.getMessage()));
    }
}

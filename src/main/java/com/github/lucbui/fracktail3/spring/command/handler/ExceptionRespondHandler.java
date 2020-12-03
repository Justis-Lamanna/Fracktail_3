package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.RespondType;
import com.github.lucbui.fracktail3.spring.command.ExceptionComponent;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ExceptionRespondHandler implements ExceptionComponent.ExceptionHandler {
    private final RespondType type;
    private final FormattedString fString;

    public ExceptionRespondHandler(RespondType type, FormattedString fString) {
        this.type = type;
        this.fString = fString;
    }

    public RespondType getType() {
        return type;
    }

    public FormattedString getfString() {
        return fString;
    }

    @Override
    public Mono<Void> apply(CommandUseContext<?> context, Throwable throwable) {
        return type.outputFString()
                .apply(context, fString, Collections.singletonMap("message", throwable.getMessage()));
    }
}

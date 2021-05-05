package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import reactor.core.publisher.Mono;

/**
 * An ExceptionHandler which responds with a certain FormattedString
 */
public class ExceptionRespondHandler implements ExceptionComponent.ECFunction {
    private final String fString;

    /**
     * Initialize
     * @param fString The string to respond with
     */
    public ExceptionRespondHandler(String fString) {
        this.fString = fString;
    }

    @Override
    public Mono<Void> apply(CommandUseContext context, Throwable throwable) {
        return context.respond(fString);
    }
}

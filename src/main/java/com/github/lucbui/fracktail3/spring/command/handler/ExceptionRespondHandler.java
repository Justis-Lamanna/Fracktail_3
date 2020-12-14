package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionBaseComponent;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * An ExceptionHandler which responds with a certain FormattedString
 */
public class ExceptionRespondHandler implements ExceptionBaseComponent.ExceptionHandler<CommandUseContext<?>> {
    private final FormattedString fString;

    /**
     * Initialize
     * Injected variables:
     * - message: The message enclosed in the Throwable
     * @param fString The string to respond with
     */
    public ExceptionRespondHandler(FormattedString fString) {
        this.fString = fString;
    }

    /**
     * Get the FormattedString being used
     * @return The FormattedString used
     */
    public FormattedString getfString() {
        return fString;
    }

    @Override
    public Mono<Void> apply(CommandUseContext<?> context, Throwable throwable) {
        return context.respond(fString, Collections.singletonMap("message", throwable.getMessage()));
    }
}

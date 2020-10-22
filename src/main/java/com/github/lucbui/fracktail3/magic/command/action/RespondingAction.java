package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.RespondingContext;
import reactor.core.publisher.Mono;

/**
 * An action which responds to the context in some way.
 * The context should implement "RespondingContext" to fully support this action.
 */
public class RespondingAction implements PlatformBasicAction {
    private final FormattedString text;

    /**
     * Initialize this action
     * @param text The text to respond with
     */
    public RespondingAction(FormattedString text) {
        this.text = text;
    }

    @Override
    public Mono<Void> doAction(PlatformBaseContext<?> context) {
        if(context instanceof RespondingContext) {
            return text.getFor(context)
                    .flatMap(((RespondingContext) context)::respond);
        }
        return Mono.empty();
    }
}

package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * An action which responds to the context in some way.
 * The context should implement "RespondingContext" to fully support this action.
 */
public class RespondingAction implements CommandAction {
    private final FormattedString text;

    /**
     * Initialize this action
     * @param text The text to respond with
     */
    public RespondingAction(FormattedString text) {
        this.text = text;
    }

    /**
     * Initialize this action
     * @param text The literal text to respond with
     */
    public RespondingAction(String text) {
        this.text = FormattedString.literal(text);
    }

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        return Mono.empty();
    }
}

package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * An action which allows composing an action with a specific guard.
 * Guarding will use a logical and between the action's internal guard, and some other guard. If either fails,
 * this action is guarded.
 */
public class CompositeAction implements CommandAction {
    private final Guard guard;
    private final CommandAction action;

    public CompositeAction(Guard guard, CommandAction action) {
        this.guard = guard;
        this.action = action;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        return action.doAction(context);
    }
}

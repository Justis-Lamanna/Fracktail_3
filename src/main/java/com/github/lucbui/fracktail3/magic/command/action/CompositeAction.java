package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * An action which allows composing an action with a specific guard.
 * Guarding will use a logical and between the action's internal guard, and some other guard. If either fails,
 * this action is guarded.
 */
public class CompositeAction implements CommandAction {
    private Guard guard;
    private CommandAction action;

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return action.doAction(context);
    }

    @Override
    public Mono<Boolean> guard(CommandUseContext<?> context) {
        return BooleanUtils.and(action.guard(context), guard.matches(context));
    }
}

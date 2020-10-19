package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.Cooldown;
import reactor.core.publisher.Mono;

/**
 * A basic decorator which enforces a basic cooldown policy.
 * If this action is invoked several times before the cooldown completes, only one (the first) is invoked.
 * All remaining calls are dropped.
 * Note that this enforces a global cooldown. Any source which calls this action is treated the same.
 * For greater control, a custom wrapper will be necessary.
 */
public class BasicCooldownActionWrapper implements Action {
    private final Cooldown cooldown;
    private final Action action;

    /**
     * Wrap an action with a cooldown
     * @param cooldown The action's cooldown
     * @param action The action to perform, when cooldown is complete.
     */
    public BasicCooldownActionWrapper(Cooldown cooldown, Action action) {
        this.cooldown = cooldown;
        this.action = action;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        if(cooldown.isCooldownComplete()) {
            return action.doAction(context)
                    .then(Mono.fromRunnable(cooldown::triggerCooldown));
        }
        return Mono.empty();
    }
}

package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * One potential arm for a parameterized action
 */
public class ActionOption implements Id, Disableable {
    private final String id;
    private final Guard guard;
    private final CommandAction action;

    private boolean enabled;

    /**
     * Initialize this action with a filter
     * @param id The ID of this branch
     * @param guard The filter to use
     * @param action The action to perform if the filter passes
     */
    public ActionOption(String id, Guard guard, CommandAction action) {
        this(id, true, guard, action);
    }

    /**
     * Initialize this action with a filter
     * @param id The ID of this branch
     * @param enabled True, if this branch is enabled
     * @param guard The filter to use
     * @param action The action to perform if the filter passes
     */
    public ActionOption(String id, boolean enabled, Guard guard, CommandAction action) {
        this.id = id;
        this.guard = guard;
        this.action = action;
        this.enabled = enabled;
    }

    /**
     * Get the filter to validate this arm
     * @return The filter user
     */
    public Guard getGuard() {
        return guard;
    }

    /**
     * Get the action to perform
     * @return The action
     */
    public CommandAction getAction() {
        return action;
    }

    /**
     * Test if this guard matches
     * @param ctx The context of the commands usage
     * @return Asynchronous boolean indicating if the guard passes
     */
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        return BooleanUtils.and(Mono.just(enabled), guard.matches(ctx));
    }

    /**
     * Unconditionally perform the action
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doAction(CommandUseContext<?> ctx) {
        return action.doAction(ctx);
    }

    /**
     * Perform action if guard passes
     * @param ctx The context of the commands usage
     * @return Asynchronous marker indicating the action finished
     */
    public Mono<Void> doActionIfPasses(CommandUseContext<?> ctx) {
        return matches(ctx)
                .flatMap(b -> b ? doAction(ctx) : Mono.empty());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
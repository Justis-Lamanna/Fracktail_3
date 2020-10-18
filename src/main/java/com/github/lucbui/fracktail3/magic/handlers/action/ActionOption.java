//package com.github.lucbui.fracktail3.magic.handlers.action;
//
//import com.github.lucbui.fracktail3.magic.*;
//import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
//import com.github.lucbui.fracktail3.magic.guards.Guard;
//import com.github.lucbui.fracktail3.magic.platform.CommandContext;
//import reactor.bool.BooleanUtils;
//import reactor.core.publisher.Mono;
//
///**
// * One potential arm for a parameterized action
// */
//public class ActionOption implements Validated, Id, Disableable {
//    private final String id;
//    private final Guard guard;
//    private final Action action;
//
//    private boolean enabled;
//
//    /**
//     * Initialize this action with a filter
//     * @param id The ID of this branch
//     * @param guard The filter to use
//     * @param action The action to perform if the filter passes
//     */
//    public ActionOption(String id, Guard guard, Action action) {
//        this(id, true, guard, action);
//    }
//
//    /**
//     * Initialize this action with a filter
//     * @param id The ID of this branch
//     * @param enabled True, if this branch is enabled
//     * @param guard The filter to use
//     * @param action The action to perform if the filter passes
//     */
//    public ActionOption(String id, boolean enabled, Guard guard, Action action) {
//        this.id = id;
//        this.guard = guard;
//        this.action = action;
//        this.enabled = enabled;
//    }
//
//    /**
//     * Get the filter to validate this arm
//     * @return The filter user
//     */
//    public Guard getGuard() {
//        return guard;
//    }
//
//    /**
//     * Get the action to perform
//     * @return The action
//     */
//    public Action getAction() {
//        return action;
//    }
//
//    /**
//     * Test if this guard matches
//     * @param bot The bot being executed
//     * @param ctx The context of the commands usage
//     * @return Asynchronous boolean indicating if the guard passes
//     */
//    public Mono<Boolean> passesFilter(Bot bot, CommandContext ctx) {
//        return BooleanUtils.and(Mono.just(enabled), guard.matches(bot, ctx));
//    }
//
//    /**
//     * Unconditionally perform the action
//     * @param bot The bot being executed
//     * @param ctx The context of the commands usage
//     * @return Asynchronous marker indicating the action finished
//     */
//    public Mono<Void> doAction(Bot bot, CommandContext ctx) {
//        return action.doAction(bot, ctx);
//    }
//
//    /**
//     * Perform action if guard passes
//     * @param bot The bot being executed
//     * @param ctx The context of the commands usage
//     * @return Asynchronous marker indicating the action finished
//     */
//    public Mono<Void> doActionIfPasses(Bot bot, CommandContext ctx) {
//        return passesFilter(bot, ctx)
//                .flatMap(b -> b ? doAction(bot, ctx) : Mono.empty());
//    }
//
//    @Override
//    public void validate(BotSpec botSpec) throws BotConfigurationException {
//        Validated.validate(guard, botSpec);
//        Validated.validate(action, botSpec);
//    }
//
//    @Override
//    public String getId() {
//        return id;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    @Override
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//}

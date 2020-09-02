//package com.github.lucbui.fracktail3.magic.handlers;
//
//import com.github.lucbui.fracktail3.magic.Bot;
//import com.github.lucbui.fracktail3.magic.BotSpec;
//import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
//import com.github.lucbui.fracktail3.magic.handlers.action.Action;
//import com.github.lucbui.fracktail3.magic.handlers.filter.BehaviorTrigger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import reactor.core.publisher.Mono;
//
//public class Behavior {
//    private static final Logger LOGGER = LoggerFactory.getLogger(Behavior.class);
//
//    private final BehaviorTrigger behaviorTrigger;
//    private final Action action;
//
//    public Behavior(BehaviorTrigger behaviorTrigger, Action action) {
//        this.behaviorTrigger = behaviorTrigger;
//        this.action = action;
//    }
//
//    public BehaviorTrigger getBehaviorTrigger() {
//        return behaviorTrigger;
//    }
//
//    public Action getAction() {
//        return action;
//    }
//
//    public Mono<Boolean> matchesTrigger(Bot bot, CommandContext<?> context) {
//        return behaviorTrigger == null ? Mono.just(true) : behaviorTrigger.matches(bot, context);
//    }
//
//    public Mono<Void> doAction(Bot bot, CommandContext<?> context){
//        LOGGER.info("Performing action: {}", action);
//        return action.doAction(bot, context);
//    }
//
//
//    public void validate(BotSpec spec, Command command) throws BotConfigurationException {
//        if(behaviorTrigger.hasRole()) {
//            String role = behaviorTrigger.getRole();
//            spec.getUsersets().flatMap(r -> r.getRoleset(role))
//                    .orElseThrow(() -> new BotConfigurationException("Behavior in " + command.getId() + " contains unknown role " + role));
//        }
//    }
//}

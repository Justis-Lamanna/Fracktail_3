package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

public class ParameterizedAction implements Action {
    private final ActionOptions actions;

    public ParameterizedAction(ActionOptions actions) {
        this.actions = actions;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return actions.doAction(bot, context);
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        actions.validate(botSpec);
    }
}

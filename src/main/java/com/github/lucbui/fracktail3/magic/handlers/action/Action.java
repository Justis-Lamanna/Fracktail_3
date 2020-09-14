package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.Validated;
import reactor.core.publisher.Mono;

public interface Action extends Validated {
    Action NOOP = (bot, context) -> Mono.empty();

    Mono<Void> doAction(Bot bot, CommandContext context);

    @Override
    default void validate(BotSpec botSpec) throws BotConfigurationException {
        //NOOP
    }
}

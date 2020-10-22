package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Action which responds to the user of the command in some way
 */
public class LoggingAction implements PlatformBasicAction, CommandAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAction.class);
    private final String msg;

    public LoggingAction(String msg) {
        this.msg = msg;
    }


    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        LOGGER.info(msg);
        return Mono.empty();
    }

    @Override
    public Mono<Void> doAction(PlatformBaseContext<?> context) {
        LOGGER.info(msg);
        return Mono.empty();
    }
}

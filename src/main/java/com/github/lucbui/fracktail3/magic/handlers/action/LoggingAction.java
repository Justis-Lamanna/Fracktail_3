package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import com.github.lucbui.fracktail3.magic.schedule.ScheduleContext;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class LoggingAction implements Action, ScheduledAction {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAction.class);

    private final String toPrint;

    public LoggingAction(String toPrint) {
        this.toPrint = toPrint;
    }

    @Override
    public Mono<Void> doAction(Bot bot, CommandContext context) {
        return log();
    }

    @Override
    public Mono<Void> execute(Bot bot, ScheduleContext context) {
        return log();
    }

    private Mono<Void> log() {
        LOGGER.debug(toPrint);
        return Mono.empty();
    }
}

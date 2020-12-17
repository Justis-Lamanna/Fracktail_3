package com.github.lucbui.fracktail3.spring.schedule.handler;

import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionBaseComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * A handler that, when invoked, cancels the scheduled event
 */
public class ExceptionCancelHandler implements ExceptionBaseComponent.ExceptionHandler<ScheduledUseContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCancelHandler.class);

    @Override
    public Mono<Void> apply(ScheduledUseContext context, Throwable throwable) {
        LOGGER.info("Cancelling event " + context.getScheduledEvent().getId(), throwable);

        context.getScheduledEvent().cancel();
        return Mono.empty();
    }
}

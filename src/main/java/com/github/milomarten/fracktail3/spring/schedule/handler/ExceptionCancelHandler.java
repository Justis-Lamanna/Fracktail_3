package com.github.milomarten.fracktail3.spring.schedule.handler;

import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.milomarten.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * A handler that, when invoked, cancels the scheduled event
 */
public class ExceptionCancelHandler implements ExceptionScheduledComponent.ECSFunction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCancelHandler.class);

    @Override
    public Mono<Void> apply(ScheduleUseContext context, Throwable throwable) {
        LOGGER.info("Cancelling event " + context.getEvent().getId(), throwable);

        context.getEvent().cancel();
        return Mono.empty();
    }
}

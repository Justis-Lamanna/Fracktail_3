package com.github.milomarten.fracktail3.spring.schedule.model;

import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.milomarten.fracktail3.spring.command.model.ExceptionBaseComponent;
import reactor.core.publisher.Mono;

/**
 * A piece of a MethodCallingScheduledAction or FieldCallingScheduledAction which handles exceptions thrown in the course of execution
 */
public class ExceptionScheduledComponent extends ExceptionBaseComponent<ExceptionScheduledComponent.ECSFunction> {
    public interface ECSFunction {
        Mono<Void> apply(ScheduleUseContext context, Throwable t);
    }
}

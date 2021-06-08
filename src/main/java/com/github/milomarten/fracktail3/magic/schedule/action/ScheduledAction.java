package com.github.milomarten.fracktail3.magic.schedule.action;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import reactor.core.publisher.Mono;

/**
 * An action which can occur on a schedule
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "_type")
public interface ScheduledAction {
    /**
     * Execute the logic
     * @param context The context of the execution
     * @return Asynchronous indication of completion
     */
    Mono<Void> execute(ScheduleUseContext context);
}

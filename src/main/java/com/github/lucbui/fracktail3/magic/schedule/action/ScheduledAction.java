package com.github.lucbui.fracktail3.magic.schedule.action;

import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import reactor.core.publisher.Mono;

/**
 * An action which can occur on a schedule
 */
public interface ScheduledAction {
    /**
     * Execute the logic
     * @param context The context of the execution
     * @return Asynchronous indication of completion
     */
    Mono<Void> execute(ScheduleUseContext context);
}

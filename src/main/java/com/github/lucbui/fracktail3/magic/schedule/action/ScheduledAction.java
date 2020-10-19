package com.github.lucbui.fracktail3.magic.schedule.action;

import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;
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
    Mono<Void> execute(ScheduledUseContext context);
}

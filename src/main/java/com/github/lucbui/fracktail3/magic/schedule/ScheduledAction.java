package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

/**
 * An action which can occur on a schedule
 */
public interface ScheduledAction {
    /**
     * Execute the logic
     * @param bot The bot being run
     * @param context The context of the execution
     * @return Asynchronous indication of completion
     */
    Mono<Void> execute(Bot bot, ScheduleContext context);
}

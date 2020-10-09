package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import reactor.core.publisher.Mono;

/**
 * Encapsulates how a platform should start and stop the bot.
 */
public interface PlatformHandler extends Id {
    /**
     * Asynchronously start the bot
     * @param bot The bot to start
     * @return An asynchronous boolean, whether the bot was started or not. (note, boolean is ignored)
     */
    Mono<Boolean> start(Bot bot);

    /**
     * Schedule an event for execution
     * @param bot The bot instance
     * @param event The event to schedule
     */
    void scheduleEvent(Bot bot, ScheduledEvent event);

    /**
     * Asynchronously stop the bot
     * @param bot The bot to stop
     * @return An asynchronous boolean, whether the bot was stopped or not. (note, boolean is ignored)
     */
    Mono<Boolean> stop(Bot bot);

    /**
     * Cancel an event
     * @param bot The bot instance
     * @param id The ID of the event to cancel
     */
    void cancelEvent(Bot bot, String id);
}

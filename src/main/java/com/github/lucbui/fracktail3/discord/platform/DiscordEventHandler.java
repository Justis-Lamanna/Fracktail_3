package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Id;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

/**
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler extends Id {
    /**
     * Check if this handler can handle this particular event
     * @param event The event to handle
     * @return True, if this code can handle the event
     */
    default Mono<Boolean> passesGuard(Event event) {
        return Mono.just(true);
    }

    /**
     * Execute the code that can handle this event
     * @param bot The bot being executed on
     * @param event The event being handled
     * @return Asynchronous indication of completion
     */
    Mono<Void> handleEvent(Bot bot, DiscordEventContext event);
}

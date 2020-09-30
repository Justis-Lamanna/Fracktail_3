package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.Event;
import org.apache.commons.lang3.ClassUtils;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

/**
 * Event Handler which handles the typical case of executing code on one event type
 * @param <E> The type of event this handler handles
 */
public abstract class DiscordSpecificEventHandler<E extends Event> implements DiscordEventHandler {
    private final Class<E> eventType;

    /**
     * Initialize this handler
     * @param eventType The type of event accepted
     */
    protected DiscordSpecificEventHandler(Class<E> eventType) {
        this.eventType = eventType;
    }

    @Override
    public Mono<Boolean> passesGuard(Event event) {
        boolean matchesEvent = ClassUtils.isAssignable(event.getClass(), eventType);
        return BooleanUtils.and(Mono.just(matchesEvent), passesSpecificGuard(eventType.cast(event)));
    }

    @Override
    public Mono<Void> handleEvent(Bot bot, DiscordEventContext context) {
        return handleSpecificEvent(bot, context, eventType.cast(context.getEvent()));
    }

    /**
     * Handle the specific event
     * @param bot The bot being executed on
     * @param context The context of the executed command
     * @param event The event which occured
     * @return Asynchronous indication of completion
     */
    public abstract Mono<Void> handleSpecificEvent(Bot bot, DiscordEventContext context, E event);

    public abstract Mono<Boolean> passesSpecificGuard(E event);
}

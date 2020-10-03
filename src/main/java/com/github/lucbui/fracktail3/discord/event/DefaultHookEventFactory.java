package com.github.lucbui.fracktail3.discord.event;

import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The default factory for converting events to HookEvents.
 * Allows support for custom HookEvents to be implemented on an Event-by-Event basis. By default,
 * simply wraps the event in a WrappedHookEvent
 */
public class DefaultHookEventFactory implements HookEventFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHookEventFactory.class);

    private final Map<Class<? extends Event>, Function<Event, HookEvent>> converters = new HashMap<>();
    private final Map<Class<? extends Event>, DiscordSupportedEvent> dseMapper;

    /**
     * Create the factory
     */
    public DefaultHookEventFactory() {
        dseMapper = Arrays.stream(DiscordSupportedEvent.values())
                .collect(Collectors.toMap(DiscordSupportedEvent::getRawEventClass, Function.identity()));
    }

    /**
     * Add a specific converter to convert events of the specified type,
     * @param event The events types to convert
     * @param converter The converter to use instead of the default
     */
    public void addConverter(DiscordSupportedEvent event, Function<Event, HookEvent> converter) {
        this.converters.put(event.getRawEventClass(), converter);
    }

    @Override
    public HookEvent fromEvent(Event event) {
        DiscordSupportedEvent dse = dseMapper.get(event.getClass());
        if(dse == null) {
            LOGGER.warn("Event {} not mapped to any DiscordSupportedEvent. Proceeding with null.", event.getClass().getCanonicalName());
        }
        if(converters.containsKey(event.getClass())) {
            return converters.get(event.getClass()).apply(event);
        }
        return new WrappedHookEvent(dse, event);
    }
}

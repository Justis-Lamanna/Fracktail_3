package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import discord4j.core.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler extends IdStore<DiscordEventHook> implements DiscordOnEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiscordOnEventHandler.class);
    private final Map<Class<?>, DiscordSupportedEvents> cache;
    private final Set<Class<?>> events;

    /**
     * Initialize this handler
     * @param handlers The sub-handlers to use
     */
    public DefaultDiscordOnEventHandler(List<DiscordEventHook> handlers) {
        super(handlers);
        cache = Arrays.stream(DiscordSupportedEvents.values())
                .collect(Collectors.toMap(DiscordSupportedEvents::getClazz, Function.identity()));

        events = handlers.stream()
                .flatMap(hook -> hook.getEvents().stream())
                .map(DiscordSupportedEvents::getClazz)
                .collect(Collectors.toSet());
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, Event event) {
        if(events.contains(event.getClass())) {
            return Flux.fromIterable(getAll())
                    .filter(DiscordEventHook::isEnabled)
                    .flatMap(hook -> execute(bot, configuration, hook, event))
                    .then();
        } else {
            return Mono.empty();
        }
    }

    private Mono<Void> execute(Bot bot, DiscordConfiguration config, DiscordEventHook hook, Event event) {
        DiscordSupportedEvents e = cache.get(event.getClass());
        if(e == null) {
            LOGGER.info("No supported handling for event of type " + event.getClass().getCanonicalName());
            return Mono.empty();
        }
        Mono<Void> executor = e.execute(bot, config, event, hook);
        if(executor == null) {
            LOGGER.info("No supported handling for event of type " + event.getClass().getCanonicalName());
            return Mono.empty();
        }
        return executor;
    }
}

package com.github.lucbui.fracktail3.discord.util.spring;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.Event;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Profile("local")
@Component
public class DiscordRegistryHook extends ReactiveEventAdapter {
    private final Map<Class<?>, AtomicInteger> counters = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private MeterRegistry registry;

    @Override
    public Publisher<?> hookOnEvent(Event event) {
        counters.computeIfAbsent(event.getClass(), c -> {
            FunctionCounter.builder("discord.events", counters, counters -> counters.get(event.getClass()).get())
                    .baseUnit("calls")
                    .description("On receipt of an event from Gateway")
                    .tags("event", event.getClass().getSimpleName().toLowerCase())
                    .register(registry);
            return new AtomicInteger();
        }).incrementAndGet();
        return Mono.empty();
    }
}

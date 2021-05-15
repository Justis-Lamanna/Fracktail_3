package com.github.lucbui.fracktail3.discord.util.spring;

import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.lifecycle.ConnectEvent;
import discord4j.core.event.domain.lifecycle.DisconnectEvent;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DiscordRegistryHook extends ReactiveEventAdapter {
    private final Map<Class<?>, AtomicInteger> counters = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    private MeterRegistry registry;

    @Getter
    private Instant lastConnect;

    @Getter
    private Instant lastDisconnect;

    @Override
    public Publisher<?> onConnect(ConnectEvent event) {
        lastConnect = Instant.now();
        return Mono.empty();
    }

    @Override
    public Publisher<?> onDisconnect(DisconnectEvent event) {
        lastDisconnect = Instant.now();
        return Mono.empty();
    }

    @Override
    public Publisher<?> hookOnEvent(Event event) {

        counters.computeIfAbsent(event.getClass(), c -> {
            String prettyName = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(
                    StringUtils.capitalize(event.getClass().getSimpleName())));
            FunctionCounter.builder("discord.events", counters, counters -> counters.get(event.getClass()).get())
                    .baseUnit("calls")
                    .description("On receipt of an event from Gateway")
                    .tags("event", prettyName)
                    .register(registry);
            return new AtomicInteger();
        }).incrementAndGet();
        return super.hookOnEvent(event);
    }
}

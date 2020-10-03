package com.github.lucbui.fracktail3.magic.hook;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import reactor.core.publisher.Mono;

import java.util.Set;

public class EventHook<E extends SupportedEvent> implements Id, Disableable {
    private final String id;
    private final Set<E> supportedEvents;
    private final EventHookGuard<E> guard;
    private final EventHookHandler<E> handler;
    private boolean enabled;

    public EventHook(String id, Set<E> supportedEvents, EventHookGuard<E> guard, EventHookHandler<E> handler, boolean enabled) {
        this.id = id;
        this.supportedEvents = supportedEvents;
        this.guard = guard;
        this.handler = handler;
        this.enabled = enabled;
    }

    @Override
    public String getId() {
        return id;
    }

    public Set<E> getSupportedEvents() {
        return supportedEvents;
    }

    public EventHookGuard<E> getGuard() {
        return guard;
    }

    public EventHookHandler<E> getHandler() {
        return handler;
    }

    public Mono<Boolean> matches(Bot bot, EventContext<E> ctx) {
        if(!enabled) {
            return Mono.just(false);
        }
        return getGuard().matches(bot, ctx);
    }

    public Mono<Void> handleEvent(Bot bot, EventContext<E> ctx) {
        return getHandler().handleEvent(bot, ctx);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

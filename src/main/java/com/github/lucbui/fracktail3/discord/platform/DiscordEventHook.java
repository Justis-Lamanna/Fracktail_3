package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.guards.DiscordEventHookGuard;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Disableable;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import discord4j.core.event.domain.Event;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

public class DiscordEventHook<E extends Event> implements Id, Disableable {
    private final Class<E> type;
    private final String id;
    private final DiscordEventHookGuard<? super E> guard;
    private final DiscordEventHandler<? super E> handler;

    private boolean enabled;

    public DiscordEventHook(Class<E> type, String id, boolean enabled, DiscordEventHookGuard<? super E> guard, DiscordEventHandler<? super E> handler) {
        this.type = type;
        this.id = id;
        this.guard = guard;
        this.handler = handler;
        this.enabled = enabled;
    }

    public Class<E> getType() {
        return type;
    }

    @Override
    public String getId() {
        return id;
    }

    public DiscordEventHookGuard<? super E> getGuard() {
        return guard;
    }

    public DiscordEventHandler<? super E> getHandler() {
        return handler;
    }

    public Mono<Boolean> passesGuard(Bot bot, DiscordEventContext ctx) {
        boolean enabledAndCorrectType = enabled && ClassUtils.isAssignable(ctx.getEvent().getClass(), type);
        if(!enabledAndCorrectType) {
            return Mono.just(false);
        }
        return getGuard().matches(bot, ctx, type.cast(ctx.getEvent()));
    }

    public Mono<Void> doAction(Bot bot, DiscordEventContext ctx) {
        return getHandler().handleEvent(bot, ctx, type.cast(ctx.getEvent()));
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Builder<E extends Event> implements IBuilder<DiscordEventHook<E>> {
        private final Class<E> type;
        private final String id;
        private DiscordEventHookGuard<? super E> guard = DiscordEventHookGuard.identity(true);
        private DiscordEventHandler<? super E> handler = DiscordEventHandler.noop();
        private boolean enabled = true;

        public Builder(String id, Class<E> type) {
            this.id = id;
            this.type = type;
        }

        public Builder<E> setGuard(DiscordEventHookGuard<? super E> guard) {
            this.guard = guard;
            return this;
        }

        public Builder<E> setHandler(DiscordEventHandler<? super E> handler) {
            this.handler = handler;
            return this;
        }

        public Builder<E> isEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @Override
        public DiscordEventHook<E> build() {
            return new DiscordEventHook<>(type, id, enabled, guard, handler);
        }
    }
}

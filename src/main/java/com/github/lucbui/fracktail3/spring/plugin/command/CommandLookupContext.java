package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.WrapperContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class CommandLookupContext<T> implements PlatformBaseContext<T>, WrapperContext {
    private final PlatformBaseContext<T> wrapped;

    public CommandLookupContext(PlatformBaseContext<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Platform getPlatform() {
        return wrapped.getPlatform();
    }

    @Override
    public Bot getBot() {
        return wrapped.getBot();
    }

    @Override
    public T getPayload() {
        return wrapped.getPayload();
    }

    @Override
    public Mono<Locale> getLocale() {
        return wrapped.getLocale();
    }

    @Override
    public AsynchronousMap<String, Object> getMap() {
        return wrapped.getMap();
    }

    @Override
    public Mono<Void> respond(String message) {
        return wrapped.respond(message);
    }

    @Override
    public BaseContext<?> getWrappedContext() {
        if(wrapped instanceof WrapperContext) {
            return ((WrapperContext) wrapped).getWrappedContext();
        }
        return wrapped;
    }
}

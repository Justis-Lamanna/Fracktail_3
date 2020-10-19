package com.github.lucbui.fracktail3.magic.guard.user;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * A mock "userset" which has is retrieved from the bot at runtime
 */
public class InUsersetGuard implements Guard {
    private final String id;
    private final boolean defaultValue;

    /**
     * Initialize UsersetById
     * @param id The ID of the Userset to retrieve
     */
    public InUsersetGuard(String id) {
        this.id = id;
        this.defaultValue = false;
    }

    /**
     * Initialize UsersetById, with default value
     * @param id The ID of the Userset to retrieve
     * @param defaultValue A default value, which is returned if the named userset does not exist.
     */
    public InUsersetGuard(String id, boolean defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    @Override
    public Mono<Boolean> matches(BaseContext<?> ctx) {
        return ctx.getPlatform().getConfig().getUserset(id)
                .map(value -> Mono.just(value).flatMap(u -> u.matches(ctx)))
                .orElseGet(() -> Mono.just(defaultValue));
    }
}

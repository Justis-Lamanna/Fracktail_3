package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.core.publisher.Mono;

/**
 * A guard which enforces if a particular key is present
 */
public class MapContainsKeyGuard implements Guard {
    private final String key;

    /**
     * Initialize
     * @param key The key to check for
     */
    public MapContainsKeyGuard(String key) {
        this.key = key;
    }

    @Override
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        if(ctx.getMap() == null) return Mono.just(false);
        return Mono.just(ctx.getMap().containsKey(key));
    }

    /**
     * Get the key being checked
     * @return The checked key
     */
    public String getKey() {
        return key;
    }
}
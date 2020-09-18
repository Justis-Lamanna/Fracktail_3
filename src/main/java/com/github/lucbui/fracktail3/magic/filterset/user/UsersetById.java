package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * A mock "userset" which has is retrieved from the bot at runtime
 */
public class UsersetById implements Filter {
    private final String id;
    private final boolean defaultValue;

    /**
     * Initialize UsersetById
     * @param id The ID of the Userset to retrieve
     */
    public UsersetById(String id) {
        this.id = id;
        this.defaultValue = true;
    }

    /**
     * Initialize UsersetById, with default value
     * @param id The ID of the Userset to retrieve
     * @param defaultValue A default value, which is returned if the named userset does not exist.
     */
    public UsersetById(String id, boolean defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    private Optional<Userset> retrieve(BotSpec spec) {
        return spec.getUserset(id);
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        return Mono.justOrEmpty(retrieve(bot.getSpec()))
                    .flatMap(u -> u.matches(bot, ctx))
                    .defaultIfEmpty(defaultValue);
    }
}

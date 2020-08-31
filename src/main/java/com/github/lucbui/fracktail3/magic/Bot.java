package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.platform.PlatformHandler;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Bot converts the BotSpec into a working bot, via use of PlatformHandler.
 * Each PlatformHandler takes the spec, and builds out the bot for that specific platform.
 * The PlatformHandler, in effect, manages the entire lifecycle for that specific platform.
 */
public class Bot {
    private final BotSpec botSpec;
    private final List<PlatformHandler> platformHandlers;

    /**
     * Initialize the Bot with specific PlatformHandlers.
     * @param botSpec The spec to use.
     * @param handlers Any number of PlatformHandlers.
     */
    public Bot(BotSpec botSpec, PlatformHandler... handlers) {
        this.botSpec = botSpec;
        this.platformHandlers = handlers.length == 0 ?
                new ArrayList<>() :
                new ArrayList<>(Arrays.asList(handlers));
    }

    /**
     * Add a PlatformHandler to the list of handlers.
     * @param handler The handler to add
     * @return This bot (for chaining)
     */
    public Bot addPlatformHandler(PlatformHandler handler) {
        platformHandlers.add(handler);
        return this;
    }

    /**
     * Get the Spec of this bot
     * @return The spec of the bot.
     */
    public BotSpec getSpec() {
        return botSpec;
    }

    /**
     * Start the bot.
     * All PlatformHandlers are initialized.
     * @return A Mono which completes when all bots have started.
     */
    public Mono<Boolean> start() {
        if(CollectionUtils.isEmpty(platformHandlers)) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.start(this))
                .then().thenReturn(true);
    }

    /**
     * Stop the bot.
     * All PlatformHandlers are halted.
     * @return A Mono which completes when all bots have stopped.
     */
    public Mono<Boolean> stop() {
        if(platformHandlers.isEmpty()) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(platformHandlers)
                .flatMap(handler -> handler.stop(this))
                .then().thenReturn(true);
    }
}

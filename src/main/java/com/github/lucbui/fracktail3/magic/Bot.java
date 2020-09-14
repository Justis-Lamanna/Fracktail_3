package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * A Bot converts the BotSpec into a working bot, via use of PlatformHandler.
 * Each PlatformHandler takes the spec, and builds out the bot for that specific platform.
 * The PlatformHandler, in effect, manages the entire lifecycle for that specific platform.
 */
public class Bot {
    private final BotSpec botSpec;

    /**
     * Initialize the Bot with specific PlatformHandlers.
     * @param botSpec The spec to use.
     */
    public Bot(BotSpec botSpec) {
        this.botSpec = botSpec;
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
        if(CollectionUtils.isEmpty(botSpec.getPlatforms())) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(botSpec.getPlatforms())
                .map(Platform::platformHandler)
                .flatMap(handler -> handler.start(this))
                .then().thenReturn(true);
    }

    /**
     * Stop the bot.
     * All PlatformHandlers are halted.
     * @return A Mono which completes when all bots have stopped.
     */
    public Mono<Boolean> stop() {
        if(CollectionUtils.isEmpty(botSpec.getPlatforms())) {
            throw new BotConfigurationException("No Handlers specified");
        }
        return Flux.fromIterable(botSpec.getPlatforms())
                .map(Platform::platformHandler)
                .flatMap(handler -> handler.stop(this))
                .then().thenReturn(true);
    }
}

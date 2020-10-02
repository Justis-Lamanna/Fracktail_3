package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;
import com.github.lucbui.fracktail3.magic.utils.model.IdStore;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A Bot converts the BotSpec into a working bot, via use of PlatformHandler.
 * Each PlatformHandler takes the spec, and builds out the bot for that specific platform.
 * The PlatformHandler, in effect, manages the entire lifecycle for that specific platform.
 */
public class Bot extends IdStore<PlatformHandler> {
    private final BotSpec botSpec;

    /**
     * Initialize the Bot with specific PlatformHandlers.
     * @param botSpec The spec to use.
     */
    public Bot(BotSpec botSpec) {
        super(botSpec.getPlatforms().stream()
                .map(Platform::platformHandler)
                .collect(Collectors.toMap(PlatformHandler::getId, Function.identity())));
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
        return Flux.fromIterable(getAll())
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
        return Flux.fromIterable(getAll())
                .flatMap(handler -> handler.stop(this))
                .then().thenReturn(true);
    }

    /**
     * Get all handlers for this bot
     * @return All the handlers used in this bot
     */
    public List<PlatformHandler> getHandlers() {
        return getAll();
    }

    /**
     * Get a handler by its ID
     * @param id The ID of the handler
     * @return The handler, if present
     */
    public Optional<PlatformHandler> getHandler(String id) {
        return getById(id);
    }
}

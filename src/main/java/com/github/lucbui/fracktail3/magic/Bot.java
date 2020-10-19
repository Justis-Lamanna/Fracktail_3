package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.schedule.DefaultScheduler;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
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
public class Bot extends IdStore<Platform> {
    private final BotSpec botSpec;
    private final Scheduler scheduler;

    /**
     * Initialize the Bot with specific PlatformHandlers.
     * @param botSpec The spec to use.
     * @param scheduler The scheduler to use for timer operations
     */
    public Bot(BotSpec botSpec, Scheduler scheduler) {
        super(botSpec.getPlatforms().stream()
                .collect(Collectors.toMap(Platform::getId, Function.identity())));
        this.botSpec = botSpec;
        this.scheduler = scheduler;
    }

    /**
     * Initialize the Bot with specific PlatformHandlers.
     * @param botSpec The spec to use.
     */
    public Bot(BotSpec botSpec) {
        this(botSpec, new DefaultScheduler());
    }

    /**
     * Get the Spec of this bot
     * @return The spec of the bot.
     */
    public BotSpec getSpec() {
        return botSpec;
    }

    /**
     * Get the scheduling service used by this bot
     * @return The scheduler service to use
     */
    public Scheduler getScheduler() {
        return scheduler;
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
    public List<Platform> getPlatforms() {
        return getAll();
    }

    /**
     * Get a handler by its ID
     * @param id The ID of the handler
     * @return The handler, if present
     */
    public Optional<Platform> getPlatform(String id) {
        return getById(id);
    }
}

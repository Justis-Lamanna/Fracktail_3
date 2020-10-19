package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.config.Config;
import reactor.core.publisher.Mono;

/**
 * An object which encapsulates a particular platform.
 * This acts as a further layer of abstracting than a PlatformHandler.
 * Platforms are now being used to identify where an event comes from,
 * rather than using the CommandContext's class.
 */
public interface Platform extends Id {
    /**
     * Get the configuration of this platform
     * @return The platform's configuration
     */
    Config getConfig();

    /**
     * Asynchronously start the bot
     * @param bot The bot to start
     * @return An asynchronous boolean, whether the bot was started or not. (note, boolean is ignored)
     */
    Mono<Boolean> start(Bot bot);

    /**
     * Asynchronously stop the bot
     * @param bot The bot to stop
     * @return An asynchronous boolean, whether the bot was stopped or not. (note, boolean is ignored)
     */
    Mono<Boolean> stop(Bot bot);
}

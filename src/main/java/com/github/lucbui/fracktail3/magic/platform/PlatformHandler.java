package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import reactor.core.publisher.Mono;

/**
 * Encapsulates how a platform should start and stop the bot.
 */
public interface PlatformHandler {
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

package com.github.milomarten.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

/**
 * Represents a generic user of the bot.
 */
public interface Person {
    /**
     * Get the name of this user
     * @return The user's name
     */
    String getName();

    /**
     * Get a place to speak to this user privately
     * @return A mono which completes when a private DM is created
     */
    Mono<Place> getPrivateChannel();

    /**
     * Check if this person is a bot or not
     * @return True if this is bot
     */
    boolean isBot();
}

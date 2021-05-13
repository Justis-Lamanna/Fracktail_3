package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Id;
import reactor.core.publisher.Mono;

import java.io.File;

/**
 * An object which encapsulates a particular platform.
 * This acts as a further layer of abstracting than a PlatformHandler.
 * Platforms are now being used to identify where an event comes from,
 * rather than using the CommandContext's class.
 */
public interface Platform extends Id {
    /**
     * Get the configuration used by this platform
     */
    Object getConfiguration();

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

    /**
     * Retrieve a person on this platform by their ID
     * @param id The person's ID
     * @return The person
     */
    Mono<Person> getPerson(String id);

    /**
     * Retrieve a place on this platform by its ID
     * @param id The place's ID
     * @return The place
     */
    Mono<Place> getPlace(String id);

    /**
     * Send a message using the specified platform
     * @param placeId The Place ID
     * @param content The content of the message
     * @param attachments Zero or more attachments
     * @return A created message
     */
    default Mono<Message> sendMessage(String placeId, String content, File... attachments) {
        return getPlace(placeId)
                .flatMap(place -> place.sendMessage(content, attachments));
    }
}

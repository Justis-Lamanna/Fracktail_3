package com.github.lucbui.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;

/**
 * Represents a message received from the bot
 */
public interface Message {
    /**
     * Get the content of the message
     * @return The message's contents
     */
    String getContent();

    /**
     * Get the URIs of the attachments
     * @return An array of URIs corresponding to attachments
     */
    URI[] getAttachments();

    /**
     * Get the user who sent this message
     * @return The sending user
     */
    Person getSender();

    /**
     * Get the place this message came from
     * @return The place this message came from
     */
    Mono<Place> getOrigin();

    /**
     * Edit this message appropriately
     * @param content The new message content
     * @return A mono which completes when the message was sent
     */
    Mono<Message> edit(String content);

    /**
     * Delete this message
     * @return A mono which completes when the message was deleted
     */
    Mono<Void> delete();

    /**
     * Reply to this message.
     * By default, this simply sends a message in the origin place
     * @param content The contents to reply with
     * @param files Any files to attach
     * @return Asynchronous reference to the created response message
     */
    default Mono<Message> reply(String content, File... files) {
        return getOrigin().flatMap(place -> place.sendMessage(content, files));
    }
}

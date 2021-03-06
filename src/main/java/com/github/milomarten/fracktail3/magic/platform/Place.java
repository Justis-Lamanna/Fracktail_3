package com.github.milomarten.fracktail3.magic.platform;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

/**
 * Represents a place where messages can be sent to and from
 */
public interface Place {
    /**
     * Get the name of this place
     * @return The place's name
     */
    String getName();

    /**
     * Send a message in this place
     * @param content The contents of the message
     * @param attachments One or more files to attach
     * @return A mono which completes when the message was sent
     */
    Mono<Message> sendMessage(String content, File... attachments);

    /**
     * Get a flow of messages originating from this place
     * @return A flux which outputs messages as they come
     */
    Flux<Message> getMessageFeed();
}

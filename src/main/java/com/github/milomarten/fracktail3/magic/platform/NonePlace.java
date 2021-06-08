package com.github.milomarten.fracktail3.magic.platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

public enum NonePlace implements Place {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(NonePlace.class);

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Mono.fromRunnable(() -> {
            LOGGER.info("Send to NonePlace: {} ({} files attached)", content, attachments.length);
        });
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.never();
    }
}

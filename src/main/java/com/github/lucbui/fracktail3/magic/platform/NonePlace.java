package com.github.lucbui.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

import java.io.File;

public enum NonePlace implements Place {
    INSTANCE;

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Mono.fromRunnable(() -> {
            System.out.printf("Send to NonePlace: %s (%d files attached)", content, attachments.length);
        });
    }
}

package com.github.lucbui.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

import java.net.URI;

public enum NoneMessage implements Message {
    INSTANCE;

    @Override
    public String getContent() {
        return "None";
    }

    @Override
    public URI[] getAttachments() {
        return new URI[0];
    }

    @Override
    public Person getSender() {
        return NonePerson.INSTANCE;
    }

    @Override
    public Mono<Place> getOrigin() {
        return Mono.just(NonePlace.INSTANCE);
    }

    @Override
    public Mono<Message> edit(String content) {
        return Mono.just(NoneMessage.INSTANCE);
    }

    @Override
    public Mono<Void> delete() {
        return Mono.empty();
    }
}

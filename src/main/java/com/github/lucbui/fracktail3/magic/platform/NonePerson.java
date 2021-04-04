package com.github.lucbui.fracktail3.magic.platform;

import reactor.core.publisher.Mono;

public enum NonePerson implements Person {
    INSTANCE;

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return Mono.just(NonePlace.INSTANCE);
    }
}

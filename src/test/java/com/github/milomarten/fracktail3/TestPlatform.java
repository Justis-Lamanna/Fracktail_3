package com.github.milomarten.fracktail3;

import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.platform.*;
import reactor.core.publisher.Mono;

public class TestPlatform implements Platform {
    @Override
    public Object getConfiguration() {
        return null;
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        return null;
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        return null;
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.just(NonePerson.INSTANCE);
    }

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.just(NonePlace.INSTANCE);
    }

    @Override
    public String getId() {
        return "test-platform";
    }
}

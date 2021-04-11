package com.github.lucbui.fracktail3.magic.platform;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MultiPlace implements Place {
    private final String name;
    private final Place[] places;

    public MultiPlace(Place... places) {
        this(Arrays.asList(places));
    }

    public MultiPlace(List<Place> places) {
        this.places = places.toArray(new Place[0]);
        this.name = places.stream()
                .map(Place::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Flux.fromArray(places)
                .flatMap(place -> place.sendMessage(content, attachments))
                .collectList()
                .map(MultiMessage::new);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.merge(
                Arrays.stream(places)
                        .map(Place::getMessageFeed)
                        .collect(Collectors.toList()));
    }
}

package com.github.lucbui.fracktail3.magic.platform;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MultiPerson implements Person {
    private final String name;
    private final Person[] persons;

    public MultiPerson(Person... persons) {
        this(Arrays.asList(persons));
    }

    public MultiPerson(List<? extends Person> persons) {
        this.persons = persons.toArray(new Person[0]);
        this.name = persons.stream()
                .map(Person::getName)
                .collect(Collectors.joining(","));
    }

    @Override
    public Mono<Place> getPrivateChannel() {
        return Flux.fromArray(persons)
                .flatMap(Person::getPrivateChannel)
                .collectList()
                .map(MultiPlace::new);
    }

    @Override
    public boolean isBot() {
        return Arrays.stream(persons).allMatch(Person::isBot);
    }
}

package com.github.lucbui.fracktail3.magic.platform;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MultiPerson implements Person, PersonGroup {
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

    @Override
    public Mono<Boolean> isInGroup(Person person) {
        return Mono.defer(() -> Mono.just(ArrayUtils.contains(this.persons, person)));
    }
}

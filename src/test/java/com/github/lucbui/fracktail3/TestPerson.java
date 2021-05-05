package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.platform.NonePlace;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TestPerson implements Person {
    private final String name;
    private Mono<Place> privateChannel = Mono.just(NonePlace.INSTANCE);
    private boolean bot = false;
}
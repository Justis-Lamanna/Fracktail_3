package com.github.milomarten.fracktail3;

import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.magic.platform.NoneMessage;
import com.github.milomarten.fracktail3.magic.platform.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TestPlace implements Place {
    private final String name;
    private Flux<Message> messageFeed = Flux.never();

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Mono.just(NoneMessage.INSTANCE);
    }
}

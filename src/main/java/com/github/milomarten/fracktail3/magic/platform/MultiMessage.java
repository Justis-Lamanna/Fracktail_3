package com.github.milomarten.fracktail3.magic.platform;

import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MultiMessage implements Message {
    private final Message[] messages;

    public MultiMessage(Message... messages) {
        this.messages = messages;
    }

    public MultiMessage(List<Message> messages) {
        this.messages = messages.toArray(new Message[0]);
    }

    @Override
    public String getContent() {
        return messages[0].getContent();
    }

    @Override
    public URI[] getAttachments() {
        return messages[0].getAttachments();
    }

    @Override
    public Person getSender() {
        return Arrays.stream(messages)
                .map(Message::getSender)
                .collect(Collectors.collectingAndThen(Collectors.toList(), MultiPerson::new));
    }

    @Override
    public Mono<Place> getOrigin() {
        return Flux.fromArray(messages)
                .flatMap(Message::getOrigin)
                .collectList()
                .map(MultiPlace::new);
    }

    @Override
    public Mono<Message> edit(String content) {
        List<Mono<Message>> edits = Arrays.stream(messages)
                .map(m -> m.edit(content))
                .collect(Collectors.toList());
        return Flux.concat(edits)
                .collectList()
                .map(MultiMessage::new);
    }

    @Override
    public Mono<Void> delete() {
        List<Mono<Void>> deletes = Arrays.stream(messages)
                .map(Message::delete)
                .collect(Collectors.toList());
        return Flux.concat(deletes)
                .next();
    }
}

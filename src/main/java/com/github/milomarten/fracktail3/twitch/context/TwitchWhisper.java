package com.github.milomarten.fracktail3.twitch.context;

import com.github.milomarten.fracktail3.magic.exception.BotConfigurationException;
import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.magic.platform.NoneMessage;
import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.milomarten.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchWhisper implements Message {
    private final TwitchClient client;
    private final PrivateMessageEvent message;

    @Override
    public String getContent() {
        return message.getMessage();
    }

    @Override
    public URI[] getAttachments() {
        return new URI[0];
    }

    @Override
    public Person getSender() {
        return new TwitchBasicPerson(client, message.getUser(), Optional.empty());
    }

    @Override
    public Mono<Place> getOrigin() {
        return Mono.just(new TwitchWhisperPlace(client, message.getUser()));
    }

    @Override
    public Mono<Message> edit(String content) {
        client.getChat().sendPrivateMessage(message.getUser().getName(), content);
        return Mono.just(NoneMessage.INSTANCE);
    }

    @Override
    public Mono<Void> delete() {
        return Mono.error(new BotConfigurationException("Cannot delete DM messages"));
    }
}

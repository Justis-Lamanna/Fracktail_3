package com.github.milomarten.fracktail3.twitch.context;

import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.magic.platform.NoneMessage;
import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.milomarten.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.net.URI;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchMessage implements Message {
    private final TwitchClient client;
    private final ChannelMessageEvent message;

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
        return new TwitchBasicPerson(client, message.getUser(), message.getMessageEvent().getTagValue("display-name"));
    }

    @Override
    public Mono<Place> getOrigin() {
        return Mono.just(new TwitchPlace(client, message.getChannel()));
    }

    @Override
    public Mono<Message> edit(String content) {
        boolean sent = message.getTwitchChat().sendMessage(message.getChannel().getName(), content);
        return sent ? Mono.just(NoneMessage.INSTANCE) : Mono.error(new RuntimeException("Unable to edit message \"" + message.getMessage() + "\""));
    }

    @Override
    public Mono<Void> delete() {
        boolean deleted = message.getTwitchChat().delete(message.getChannel().getName(), message.getEventId());
        return deleted ? Mono.empty() : Mono.error(new RuntimeException("Unable to delete message \"" + message.getMessage() + "\""));
    }
}

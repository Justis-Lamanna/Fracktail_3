package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NoneMessage;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.lucbui.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchEverywhere implements Place {
    private final TwitchClient client;

    @Override
    public String getName() {
        return "Everywhere";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Mono.just(NoneMessage.INSTANCE);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.push(sink -> {
             client.getEventManager().onEvent(ChannelMessageEvent.class, e -> {
                 sink.next(new TwitchMessage(client, e));
             });
             client.getEventManager().onEvent(PrivateMessageEvent.class, e -> {
                 sink.next(new TwitchWhisper(client, e));
             });
        });
    }
}

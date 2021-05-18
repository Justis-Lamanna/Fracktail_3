package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NoneMessage;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
public class TwitchPlace implements Place {
    private final TwitchClient client;
    private final EventChannel channel;

    @Override
    public String getName() {
        return channel.getName();
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        boolean sent = client.getChat().sendMessage(channel.getName(), content);
        return sent ? Mono.just(NoneMessage.INSTANCE) : Mono.error(new RuntimeException("Unable to send message \"" + content + "\""));
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.push(sink -> {
            client.getEventManager().onEvent(ChannelMessageEvent.class, e -> {
                if(e.getChannel().getId().equals(channel.getId())) {
                    sink.next(new TwitchMessage(client, e));
                }
            });
        });
    }
}

package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NoneMessage;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import com.github.lucbui.fracktail3.twitch.platform.TwitchPlatform;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.events.user.PrivateMessageEvent;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
@PlatformModel(TwitchPlatform.class)
public class TwitchWhisperPlace implements Place {
    private final TwitchClient client;
    private final EventUser user;

    @Override
    public String getName() {
        return user.getName() + "'s DMs";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        client.getChat().sendPrivateMessage(user.getName(), content);
        return Mono.just(NoneMessage.INSTANCE);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.push(sink -> {
            client.getEventManager().onEvent(PrivateMessageEvent.class, e -> {
                if(e.getUser().getId().equals(user.getId())) {
                    sink.next(new TwitchWhisper(client, e));
                }
            });
        });
    }
}

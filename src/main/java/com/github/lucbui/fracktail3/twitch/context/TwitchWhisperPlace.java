package com.github.lucbui.fracktail3.twitch.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NoneMessage;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.twitch4j.TwitchClient;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
public class TwitchWhisperPlace implements Place {
    private final TwitchClient client;
    private final String username;

    @Override
    public String getName() {
        return username + "'s DMs";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        client.getChat().sendPrivateMessage(username, content);
        return Mono.just(NoneMessage.INSTANCE);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return Flux.never();
    }
}

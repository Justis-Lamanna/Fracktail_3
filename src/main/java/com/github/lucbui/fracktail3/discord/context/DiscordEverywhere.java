package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Data
public class DiscordEverywhere implements Place {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordEverywhere.class);

    private GatewayDiscordClient client;

    @Override
    public String getName() {
        return "Everywhere";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return Mono.fromRunnable(() -> {
            LOGGER.info("Send to DiscordEverywhere: {} ({} files attached)", content, attachments.length);
        });
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return client.on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .map(DiscordMessage::new);
    }
}

package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.MultiMessage;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.MessageCreateSpec;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Represents 'everywhere' in Discord
 *
 * Note that this is just wherever the bot is.
 * Sending messages to everywhere does nothing.
 */
@Data
public class DiscordEverywhere implements Place {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordEverywhere.class);

    private final GatewayDiscordClient client;

    @Override
    public String getName() {
        return "Everywhere";
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return client.getGuilds()
                .flatMap(Guild::getSystemChannel)
                .flatMap(tc -> tc.createMessage(spec -> createSpec(spec, content, attachments)))
                .map(DiscordMessage::new)
                .cast(Message.class)
                .collectList()
                .map(MultiMessage::new);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return client.on(MessageCreateEvent.class)
                .map(DiscordMessage::new);
    }

    private static void createSpec(MessageCreateSpec spec, String content, File... attachments) {
        spec.setContent(content);
        try {
            for (File file : attachments) {
                spec.addFile(file.getName(), new FileInputStream(file));
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("Unknown file", ex);
        }
    }
}

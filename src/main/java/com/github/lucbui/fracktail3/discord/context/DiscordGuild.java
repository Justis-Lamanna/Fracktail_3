package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.MessageCreateSpec;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A wrapper around a Discord Guild
 *
 * Messages sent to this place are posted to the System Channel of the corresponding Guild.
 */
@Data
public class DiscordGuild implements Place {
    private final Guild guild;

    @Override
    public String getName() {
        return guild.getName();
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return guild.getSystemChannel()
                .flatMap(tc -> tc.createMessage(spec -> createSpec(spec, content, attachments)))
                .map(DiscordMessage::new);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return guild.getClient().on(MessageCreateEvent.class)
                .filter(mce -> mce.getGuildId().map(id -> guild.getId().equals(id)).orElse(false))
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

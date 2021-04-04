package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageCreateSpec;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

@Data
public class DiscordPlace implements Place {
    private final String name;
    private final MessageChannel place;

    public DiscordPlace(MessageChannel place) {
        if(place instanceof TextChannel) {
            this.name = ((TextChannel) place).getName();
        } else if(place instanceof PrivateChannel) {
            this.name = ((PrivateChannel) place).getRecipientIds().stream()
                    .map(Snowflake::asString)
                    .collect(Collectors.joining(",", "DMs of: ", ""));
        } else {
            this.name = place.getId().asString();
        }
        this.place = place;
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return place.createMessage(spec -> createSpec(spec, content, attachments))
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

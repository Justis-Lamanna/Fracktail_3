package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Formattable;
import java.util.FormattableFlags;
import java.util.Formatter;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A wrapper around a Discord Channel
 * Note: You can use this as the input to a format string. %s will display the channel's name,
 * and %#s will print a mention output ("#channel"). All other flags, width, and precision are ignored.
 */
@Data
public class DiscordPlace implements Place, Formattable {
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

    public Mono<Message> sendEmbed(Consumer<? super EmbedCreateSpec> spec) {
        return place.createEmbed(spec)
                .map(DiscordMessage::new);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return place.getClient()
                .on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(m -> m.getChannelId().equals(place.getId()))
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

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {
        boolean alternate = (flags & FormattableFlags.ALTERNATE) == FormattableFlags.ALTERNATE;
        String output = alternate ? place.getMention() : getName();
        formatter.format(output);
    }
}

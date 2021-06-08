package com.github.milomarten.fracktail3.discord.context;

import com.github.milomarten.fracktail3.discord.platform.DiscordPlatform;
import com.github.milomarten.fracktail3.discord.util.DiscordUtils;
import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.magic.platform.Place;
import com.github.milomarten.fracktail3.spring.command.annotation.strategy.PlatformModel;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

/**
 * A wrapper around a Discord Guild
 *
 * Messages sent to this place are posted to the System Channel of the corresponding Guild.
 */
@Data
@PlatformModel(DiscordPlatform.class)
public class DiscordGuild implements Place {
    private final Guild guild;

    @Override
    public String getName() {
        return guild.getName();
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return guild.getSystemChannel()
                .flatMap(tc -> tc.createMessage(spec -> DiscordUtils.createSpec(spec, content, attachments)))
                .map(DiscordMessage::new);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return guild.getClient().on(MessageCreateEvent.class)
                .filter(mce -> mce.getGuildId().map(id -> guild.getId().equals(id)).orElse(false))
                .map(DiscordMessage::new);
    }
}

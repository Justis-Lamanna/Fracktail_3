package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.platform.*;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

/**
 * Wrapper around a text message in Discord
 */
@Data
public class DiscordMessage implements Message {
    private String content;
    private URI[] attachments;
    private Person sender;
    private Place sentFrom;

    private discord4j.core.object.entity.Message wrappedMsg;
    private MessageCreateEvent event;

    public DiscordMessage(discord4j.core.object.entity.Message message) {
        this.wrappedMsg = message;
        this.content = message.getContent();
        this.attachments = message.getAttachments().stream()
                .map(a -> URI.create(a.getUrl()))
                .toArray(URI[]::new);
        this.sender = message.getAuthor()
                .map(u -> (Person) (u.isBot() ? new DiscordBotPerson(u) : new DiscordPerson(u)))
                .orElse(NonePerson.INSTANCE);
        this.sentFrom = message.getChannel()
                .map(DiscordPlace::new)
                .cast(Place.class)
                .blockOptional(Duration.ofSeconds(5))
                .orElse(NonePlace.INSTANCE);
    }

    @Override
    public Mono<Message> edit(String content) {
        return wrappedMsg.edit(spec -> spec.setContent(this.content))
                .map(DiscordMessage::new);
    }

    @Override
    public Mono<Void> delete() {
        return wrappedMsg.delete();
    }
}

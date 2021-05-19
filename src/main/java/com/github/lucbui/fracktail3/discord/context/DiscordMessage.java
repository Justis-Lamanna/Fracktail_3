package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.util.DiscordUtils;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NonePerson;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import discord4j.core.event.domain.message.MessageCreateEvent;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;

/**
 * Wrapper around a text message in Discord
 */
@Data
@PlatformModel(DiscordPlatform.class)
public class DiscordMessage implements Message {
    private String content;
    private URI[] attachments;
    private Person sender;

    private discord4j.core.object.entity.Message wrappedMsg;
    private MessageCreateEvent event;

    public DiscordMessage(discord4j.core.object.entity.Message message) {
        this.wrappedMsg = message;
        this.content = message.getContent();
        this.attachments = message.getAttachments().stream()
                .map(a -> URI.create(a.getUrl()))
                .toArray(URI[]::new);
        this.sender = message.getAuthor()
                .map(u -> (Person) new DiscordPerson(u))
                .orElse(NonePerson.INSTANCE);
    }

    public DiscordMessage(MessageCreateEvent event) {
        this.wrappedMsg = event.getMessage();
        this.content = event.getMessage().getContent();
        this.attachments = event.getMessage().getAttachments().stream()
                .map(a -> URI.create(a.getUrl()))
                .toArray(URI[]::new);
        if(event.getMember().isPresent()) {
            this.sender = new DiscordPerson(event.getMember().get());
        } else if(event.getMessage().getAuthor().isPresent()) {
            this.sender = new DiscordPerson(event.getMessage().getAuthor().get());
        } else {
            this.sender = NonePerson.INSTANCE;
        }
    }

    @Override
    public Mono<Place> getOrigin() {
        return wrappedMsg.getChannel()
                .map(DiscordPlace::new)
                .cast(Place.class);
    }

    @Override
    public Mono<Message> reply(String content, File... attachments) {
        return wrappedMsg.getChannel()
                .flatMap(mc -> mc.createMessage(spec -> {
                    spec.setMessageReference(wrappedMsg.getId());
                    DiscordUtils.createSpec(spec, content, attachments);;
                }))
                .map(DiscordMessage::new);
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

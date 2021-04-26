package com.github.lucbui.fracktail3.discord.context.slash;

import com.github.lucbui.fracktail3.discord.context.DiscordPerson;
import com.github.lucbui.fracktail3.discord.context.DiscordPlace;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.event.domain.InteractionCreateEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.net.URI;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscordSlashMessage implements Message {
    private final InteractionCreateEvent ice;
    private long flow = 0;

    @Override
    public String getContent() {
        return ice.getCommandName();
    }

    @Override
    public URI[] getAttachments() {
        return new URI[0];
    }

    @Override
    public Person getSender() {
        return ice.getInteraction()
                .getMember().map(DiscordPerson::new)
                .orElse(new DiscordPerson(ice.getInteraction().getUser()));
    }

    @Override
    public Mono<Place> getOrigin() {
        return ice.getInteraction()
                .getChannel()
                .map(DiscordPlace::new);
    }

    @Override
    public Mono<Message> edit(String content) {
        return ice.getInteractionResponse()
                .createFollowupMessage(content)
                .thenReturn(new DiscordSlashMessage(ice, flow + 1));
    }

    @Override
    public Mono<Void> delete() {
        return ice.getInteractionResponse()
                .deleteFollowupMessage(this.flow);
    }
}

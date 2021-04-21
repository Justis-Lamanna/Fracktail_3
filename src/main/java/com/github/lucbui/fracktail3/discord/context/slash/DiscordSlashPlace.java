package com.github.lucbui.fracktail3.discord.context.slash;

import com.github.lucbui.fracktail3.discord.context.DiscordPlace;
import com.github.lucbui.fracktail3.magic.platform.Message;
import com.github.lucbui.fracktail3.magic.platform.NoneMessage;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

@Getter
@EqualsAndHashCode(callSuper = true)
public class DiscordSlashPlace extends DiscordPlace {
    private final InteractionCreateEvent ice;

    public DiscordSlashPlace(MessageChannel place, InteractionCreateEvent ice) {
        super(place);
        this.ice = ice;
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return ice.acknowledge()
                .then(ice.reply(content))
                .thenReturn(NoneMessage.INSTANCE);
    }

    @Override
    public Flux<Message> getMessageFeed() {
        return getPlace().getClient().on(InteractionCreateEvent.class)
                .map(DiscordSlashMessage::new);
    }
}

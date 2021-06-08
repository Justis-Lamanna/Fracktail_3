package com.github.milomarten.fracktail3.discord.context.slash;

import com.github.milomarten.fracktail3.discord.context.DiscordPlace;
import com.github.milomarten.fracktail3.discord.platform.DiscordPlatform;
import com.github.milomarten.fracktail3.magic.platform.Message;
import com.github.milomarten.fracktail3.spring.command.annotation.strategy.PlatformModel;
import discord4j.core.event.domain.InteractionCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.io.File;

@Getter
@EqualsAndHashCode(callSuper = true)
@PlatformModel(DiscordPlatform.class)
public class DiscordSlashPlace extends DiscordPlace {
    private final InteractionCreateEvent ice;

    public DiscordSlashPlace(MessageChannel channel, InteractionCreateEvent ice) {
        super(channel);
        this.ice = ice;
    }

    @Override
    public Mono<Message> sendMessage(String content, File... attachments) {
        return ice.acknowledge()
                .then(ice.getInteractionResponse().createFollowupMessage(content))
                .thenReturn(new DiscordSlashMessage(ice));
    }
}

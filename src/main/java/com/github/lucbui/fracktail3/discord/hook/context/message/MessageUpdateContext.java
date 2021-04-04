package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.context.DiscordPlace;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class MessageUpdateContext extends DiscordBasePlatformContext<MessageUpdateEvent> {
    public MessageUpdateContext(Bot bot, DiscordPlatform platform, MessageUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public MessageUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, MessageUpdateEvent payload) {
        super(bot, platform, payload);
    }

    @Override
    public Mono<Place> getTriggerPlace() {
        return getPayload().getMessage().flatMap(Message::getChannel).map(DiscordPlace::new);
    }
}

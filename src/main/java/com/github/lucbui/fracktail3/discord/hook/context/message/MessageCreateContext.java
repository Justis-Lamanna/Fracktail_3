package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.context.DiscordPlace;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Place;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class MessageCreateContext extends DiscordBasePlatformContext<MessageCreateEvent> {
    public MessageCreateContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    public MessageCreateContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    @Override
    public Mono<Place> getTriggerPlace() {
        return getPayload().getMessage().getChannel().map(DiscordPlace::new);
    }
}

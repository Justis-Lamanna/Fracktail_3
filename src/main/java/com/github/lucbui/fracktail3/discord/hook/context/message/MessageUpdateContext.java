package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.context.RespondingContext;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import reactor.core.publisher.Mono;

import java.util.Locale;

public class MessageUpdateContext extends DiscordBasePlatformContext<MessageUpdateEvent> implements RespondingContext {
    public MessageUpdateContext(Bot bot, DiscordPlatform platform, MessageUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public MessageUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, MessageUpdateEvent payload) {
        super(bot, platform, payload);
    }

    @Override
    public Mono<Void> respond(String message) {
        return getPlatform()
                .message(DiscordChannelset.forChannel(getPayload().getChannelId()), message);
    }
}

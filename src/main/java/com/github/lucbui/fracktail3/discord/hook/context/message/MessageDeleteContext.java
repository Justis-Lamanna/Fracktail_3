package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.MessageDeleteEvent;

import java.util.Locale;

public class MessageDeleteContext extends DiscordBasePlatformContext<MessageDeleteEvent> {
    public MessageDeleteContext(Bot bot, DiscordPlatform platform, MessageDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public MessageDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, MessageDeleteEvent payload) {
        super(bot, platform, payload);
    }
}

package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.MessageBulkDeleteEvent;

import java.util.Locale;

public class MessageBulkDeleteContext extends DiscordBasePlatformContext<MessageBulkDeleteEvent> {
    public MessageBulkDeleteContext(Bot bot, DiscordPlatform platform, MessageBulkDeleteEvent payload) {
        super(bot, platform, payload);
    }

    public MessageBulkDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, MessageBulkDeleteEvent payload) {
        super(bot, platform, payload);
    }
}

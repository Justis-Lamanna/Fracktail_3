package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Locale;

public class DiscordCommandSearchContext extends DiscordBaseContext<MessageCreateEvent> {
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}

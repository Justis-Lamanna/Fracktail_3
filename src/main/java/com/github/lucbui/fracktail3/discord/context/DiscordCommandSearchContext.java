package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.Locale;

public class DiscordCommandSearchContext extends DiscordBaseContext<MessageCreateEvent> {
    public DiscordCommandSearchContext(Bot bot, Platform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    public DiscordCommandSearchContext(Bot bot, Platform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, locale, payload);
    }
}

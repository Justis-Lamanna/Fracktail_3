package com.github.lucbui.fracktail3.discord.hook.context.user;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.PresenceUpdateEvent;

import java.util.Locale;

public class PresenceUpdateContext extends DiscordBasePlatformContext<PresenceUpdateEvent> {
    public PresenceUpdateContext(Bot bot, DiscordPlatform platform, PresenceUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public PresenceUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, PresenceUpdateEvent payload) {
        super(bot, platform, payload);
    }
}

package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.EmojisUpdateEvent;

import java.util.Locale;

public class EmojiUpdateContext extends DiscordBasePlatformContext<EmojisUpdateEvent> {
    public EmojiUpdateContext(Bot bot, DiscordPlatform platform, EmojisUpdateEvent payload) {
        super(bot, platform, payload);
    }

    public EmojiUpdateContext(Bot bot, DiscordPlatform platform, Locale locale, EmojisUpdateEvent payload) {
        super(bot, platform, payload);
    }
}

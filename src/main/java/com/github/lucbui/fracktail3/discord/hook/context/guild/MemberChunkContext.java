package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.MemberChunkEvent;

import java.util.Locale;

public class MemberChunkContext extends DiscordBasePlatformContext<MemberChunkEvent> {
    public MemberChunkContext(Bot bot, DiscordPlatform platform, MemberChunkEvent payload) {
        super(bot, platform, payload);
    }

    public MemberChunkContext(Bot bot, DiscordPlatform platform, Locale locale, MemberChunkEvent payload) {
        super(bot, platform, locale, payload);
    }
}

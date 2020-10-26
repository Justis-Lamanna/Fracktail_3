package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.guild.MemberLeaveEvent;

import java.util.Locale;

public class MemberLeaveContext extends DiscordBasePlatformContext<MemberLeaveEvent> {
    public MemberLeaveContext(Bot bot, DiscordPlatform platform, MemberLeaveEvent payload) {
        super(bot, platform, payload);
    }

    public MemberLeaveContext(Bot bot, DiscordPlatform platform, Locale locale, MemberLeaveEvent payload) {
        super(bot, platform, locale, payload);
    }
}

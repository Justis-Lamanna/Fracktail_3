package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;

import java.util.Locale;

public class MemberLeaveContext extends DiscordBasePlatformContext<MemberLeaveContext> {
    public MemberLeaveContext(Bot bot, DiscordPlatform platform, MemberLeaveContext payload) {
        super(bot, platform, payload);
    }

    public MemberLeaveContext(Bot bot, DiscordPlatform platform, Locale locale, MemberLeaveContext payload) {
        super(bot, platform, locale, payload);
    }
}

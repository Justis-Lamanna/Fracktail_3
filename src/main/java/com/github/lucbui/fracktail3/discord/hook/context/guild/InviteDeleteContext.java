package com.github.lucbui.fracktail3.discord.hook.context.guild;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;

import java.util.Locale;

public class InviteDeleteContext extends DiscordBasePlatformContext<InviteDeleteContext> {
    public InviteDeleteContext(Bot bot, DiscordPlatform platform, InviteDeleteContext payload) {
        super(bot, platform, payload);
    }

    public InviteDeleteContext(Bot bot, DiscordPlatform platform, Locale locale, InviteDeleteContext payload) {
        super(bot, platform, locale, payload);
    }
}

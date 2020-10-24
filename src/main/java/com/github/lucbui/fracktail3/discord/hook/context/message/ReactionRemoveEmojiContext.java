package com.github.lucbui.fracktail3.discord.hook.context.message;

import com.github.lucbui.fracktail3.discord.context.DiscordBasePlatformContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.message.ReactionRemoveEmojiEvent;

import java.util.Locale;

public class ReactionRemoveEmojiContext extends DiscordBasePlatformContext<ReactionRemoveEmojiEvent> {
    public ReactionRemoveEmojiContext(Bot bot, DiscordPlatform platform, ReactionRemoveEmojiEvent payload) {
        super(bot, platform, payload);
    }

    public ReactionRemoveEmojiContext(Bot bot, DiscordPlatform platform, Locale locale, ReactionRemoveEmojiEvent payload) {
        super(bot, platform, locale, payload);
    }
}

package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;

import java.util.Locale;

public class DiscordBasePlatformContext<T> implements PlatformBaseContext<T> {
    private final Bot bot;
    private final DiscordPlatform platform;
    private final Locale locale;
    private final T payload;

    public DiscordBasePlatformContext(DiscordBasePlatformContext<T> base) {
        this.bot = base.getBot();
        this.platform = base.getPlatform();
        this.locale = base.getLocale();
        this.payload = base.getPayload();
    }

    public DiscordBasePlatformContext(Bot bot, DiscordPlatform platform, T payload) {
        this.bot = bot;
        this.platform = platform;
        this.payload = payload;
        this.locale = Locale.getDefault();
    }

    public DiscordBasePlatformContext(Bot bot, DiscordPlatform platform, Locale locale, T payload) {
        this.bot = bot;
        this.platform = platform;
        this.locale = locale;
        this.payload = payload;
    }

    @Override
    public Bot getBot() {
        return bot;
    }

    @Override
    public DiscordPlatform getPlatform() {
        return platform;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}

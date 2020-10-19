package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;

import java.util.Locale;

public class DiscordBaseContext<T> implements BaseContext<T> {
    private final Bot bot;
    private final DiscordPlatform platform;
    private final Locale locale;
    private final T payload;

    public DiscordBaseContext(DiscordBaseContext<T> base) {
        this.bot = base.getBot();
        this.platform = base.getPlatform();
        this.locale = base.getLocale();
        this.payload = base.getPayload();
    }

    public DiscordBaseContext(Bot bot, DiscordPlatform platform, T payload) {
        this.bot = bot;
        this.platform = platform;
        this.payload = payload;
        this.locale = Locale.getDefault();
    }

    public DiscordBaseContext(Bot bot, DiscordPlatform platform, Locale locale, T payload) {
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

package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;

import java.util.Locale;
import java.util.ResourceBundle;

public class DiscordBasePlatformContext<T> implements PlatformBaseContext<T>, Localizable {
    private final Bot bot;
    private final DiscordPlatform platform;
    private final T payload;

    public DiscordBasePlatformContext(DiscordBasePlatformContext<T> base) {
        this.bot = base.getBot();
        this.platform = base.getPlatform();
        this.payload = base.getPayload();
    }

    public DiscordBasePlatformContext(Bot bot, DiscordPlatform platform, T payload) {
        this.bot = bot;
        this.platform = platform;
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
    public ResourceBundle getResourceBundle(Locale locale) {
        return platform.getConfig().getLocalizationBundle(locale);
    }

    @Override
    public boolean isLocalizationEnabled() {
        return platform.getConfig().isLocalizationEnabled();
    }
}

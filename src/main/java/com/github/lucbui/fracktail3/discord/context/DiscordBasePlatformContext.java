package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.platform.NonePlace;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.ResourceBundle;

public class DiscordBasePlatformContext<T> implements PlatformBaseContext<T>, Localizable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBasePlatformContext.class);

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
    public Mono<Place> getTriggerPlace() {
        return Mono.just(NonePlace.INSTANCE);
    }

    @Override
    public ResourceBundle getResourceBundle(Locale locale) {
        return platform.getConfig().getResourceBundle(locale);
    }

    @Override
    public boolean isLocalizationEnabled() {
        return platform.getConfig().isLocalizationEnabled();
    }

    @Override
    public String toString() {
        return "DiscordBasePlatformContext{" +
                "bot=" + bot +
                ", platform=" + platform +
                ", payload=" + payload +
                '}';
    }
}

package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;

/**
 * A singleton which represents the Discord platform
 */
public class DiscordPlatform implements Platform<DiscordConfiguration> {
    private final DiscordConfiguration configuration;

    public DiscordPlatform(DiscordConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public DiscordConfiguration getConfig() {
        return configuration;
    }

    @Override
    public PlatformHandler platformHandler() {
        return new DiscordPlatformHandler(this);
    }
}

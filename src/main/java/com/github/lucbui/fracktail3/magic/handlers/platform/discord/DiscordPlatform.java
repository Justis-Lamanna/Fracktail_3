package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;
import com.github.lucbui.fracktail3.magic.handlers.platform.PlatformHandler;

public enum DiscordPlatform implements Platform<DiscordConfiguration> {
    INSTANCE;

    @Override
    public String id() {
        return "Discord";
    }

    @Override
    public PlatformHandler platformHandler() {
        return new DiscordPlatformHandler();
    }
}

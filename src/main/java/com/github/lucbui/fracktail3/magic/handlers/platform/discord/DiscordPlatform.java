package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.platform.Platform;

public enum DiscordPlatform implements Platform<DiscordConfiguration> {
    INSTANCE;

    @Override
    public String id() {
        return "Discord";
    }
}

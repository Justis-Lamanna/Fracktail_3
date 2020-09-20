package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;

/**
 * A singleton which represents the Discord platform
 */
public enum DiscordPlatform implements Platform<DiscordConfiguration> {
    /**
     * The singleton instance of this platform.
     */
    INSTANCE;

    @Override
    public String getId() {
        return "discord";
    }

    @Override
    public Class<DiscordConfiguration> getConfigClass() {
        return DiscordConfiguration.class;
    }

    @Override
    public PlatformHandler platformHandler() {
        return new DiscordPlatformHandler();
    }
}

package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;

/**
 * A singleton which represents the Discord platform
 */
public class DiscordPlatform implements Platform {
    private final DiscordConfiguration configuration;

    /**
     * Initialize this platform with a configuration
     * @param configuration The configuration to use
     */
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

    public static class Builder implements IBuilder<DiscordPlatform> {
        private DiscordConfiguration configuration;

        public Builder withConfiguration(DiscordConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public Builder withConfiguration(IBuilder<DiscordConfiguration> configuration) {
            this.configuration = configuration.build();
            return this;
        }

        @Override
        public DiscordPlatform build() {
            return new DiscordPlatform(configuration);
        }
    }
}

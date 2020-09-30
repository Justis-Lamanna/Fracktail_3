package com.github.lucbui.fracktail3.discord.platform;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.BotCreatorAware;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.PlatformHandler;

/**
 * A singleton which represents the Discord platform
 */
public class DiscordPlatform implements Platform<DiscordConfiguration>, BotCreatorAware {
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

    @Override
    public void configure(BotCreator creator) {
        configuration.configure(creator);
    }
}

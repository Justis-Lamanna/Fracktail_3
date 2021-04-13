package com.github.lucbui.fracktail3.discord.config;

import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Builder to construct a DiscordConfiguration
 */
public class DiscordConfigurationBuilder implements IBuilder<DiscordConfiguration> {
    private final String token;
    private String prefix;
    private StatusUpdate presence;
    private String i18nPath;

    /**
     * Initialize builder with a token
     * @param token The token for your bot to use
     */
    public DiscordConfigurationBuilder(String token) {
        this.token = token;
    }

    /**
     * Set the prefix commands should begin with
     * @param prefix The prefix to use.
     * @return This builder.
     */
    public DiscordConfigurationBuilder withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Set the presence of this bot
     * If unspecified, the default is a simple online status.
     * @param presence The bot's presence
     * @return This builder.
     */
    public DiscordConfigurationBuilder withPresence(StatusUpdate presence) {
        this.presence = presence;
        return this;
    }

    /**
     * Specify the path of a Resource Bundle.
     * If this path is absent, localization is disabled. If present, ResourceBundles can be retrieved from
     * the provided path on a locale-by-locale basis. These bundles can then be used in translation of a FormattedString
     * @param path The base path of the resource bundles
     * @return This builder.
     */
    public DiscordConfigurationBuilder withI18N(String path) {
        this.i18nPath = path;
        return this;
    }

    @Override
    public DiscordConfiguration build() {
        return new DiscordConfiguration(
                token,
                ObjectUtils.defaultIfNull(prefix, ""),
                i18nPath,
                ObjectUtils.defaultIfNull(presence, Presence.online()));
    }
}

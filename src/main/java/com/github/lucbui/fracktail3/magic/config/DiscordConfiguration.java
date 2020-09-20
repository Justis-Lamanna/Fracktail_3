package com.github.lucbui.fracktail3.magic.config;

import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.BotCreatorAware;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.filterset.user.DiscordUserset;
import com.github.lucbui.fracktail3.magic.filterset.user.InUsersetFilter;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Subclass of a Configuration for a Discord bot.
 */
public class DiscordConfiguration implements Config, BotCreatorAware, Localizable {
    public static final String OWNER_USERSET_ID = "owner";
    public static final Filter OWNER_FILTER = new InUsersetFilter(OWNER_USERSET_ID);

    private final String token;
    private final String prefix;
    private final Snowflake owner;
    private final StatusUpdate presence;
    private final String i18nPath;

    /**
     * Initialize a bot Configuration
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     * @param owner The owner of this bot (can be null).
     * @param i18nPath The path for a Localization bundle.
     * @param presence The presence this bot should have.
     */
    public DiscordConfiguration(String token, String prefix, @Nullable Snowflake owner, String i18nPath, StatusUpdate presence) {
        this.token = token;
        this.prefix = prefix;
        this.owner = owner;
        this.presence = presence;
        this.i18nPath = i18nPath;
    }

    /**
     * Initialize a bot Configuration with no owner or i18n.
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     * @param presence The presence this bot should have.
     */
    public DiscordConfiguration(String token, String prefix, StatusUpdate presence) {
        this(token, prefix, null, null, presence);
    }

    /**
     * Initialize a bot Configuration with no owner or i18n, and a general online presence.
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     */
    public DiscordConfiguration(String token, String prefix) {
        this(token, prefix, null, null, Presence.online());
    }

    @Override
    public Optional<String> getTextForKey(String key, Locale locale) {
        return getResourceBundle(locale)
                .filter(bundle -> bundle.containsKey(key))
                .map(bundle -> bundle.getString(key));
    }

    /**
     * Get the token of this bot.
     * @return The bot's token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Get the prefix of this bot.
     * @return The bot's prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get the presence of this bot.
     * @return The bot's presence.
     */
    public StatusUpdate getPresence() {
        return presence;
    }

    /**
     * Get the owner of the bot, if it exists.
     * @return The bot's owner, or empty if none registered.
     */
    public Optional<Snowflake> getOwner() {
        return Optional.ofNullable(owner);
    }

    /**
     * Get the path to the resource bundle.
     * @return The resource bundle's path, or empty if none registered.
     */
    public Optional<String> getI18nPath() {
        return Optional.ofNullable(i18nPath);
    }

    /**
     * Test is i18n is enabled.
     * @return True, if i18n is supported.
     */
    public boolean hasI18nEnabled() {
        return i18nPath != null;
    }

    /**
     * Get the ResourceBundle for a particular locale.
     * @param locale The locale to retrieve.
     * @return A ResourceBundle, if i18n is supported.
     */
    public Optional<ResourceBundle> getResourceBundle(Locale locale) {
        return getI18nPath().map(path -> ResourceBundle.getBundle(path, locale));
    }

    @Override
    public void configure(BotCreator creator) {
        if(owner != null) {
            creator.withUserset(DiscordUserset.forUser(OWNER_USERSET_ID, owner));
        }
    }

    @Override
    public ResourceBundle getBundle(Locale locale) {
        if(!isEnabled()) {
            throw new NoSuchElementException("Localization is disabled");
        }
        return ResourceBundle.getBundle(i18nPath, locale);
    }

    @Override
    public boolean isEnabled() {
        return i18nPath != null;
    }

    public static class Builder implements IBuilder<DiscordConfiguration> {
        private final String token;
        private String prefix;
        private Snowflake owner;
        private StatusUpdate presence;
        private String i18nPath;

        public Builder(String token) {
            this.token = token;
        }

        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder withOwner(Snowflake owner) {
            this.owner = owner;
            return this;
        }

        public Builder withPresence(StatusUpdate presence) {
            this.presence = presence;
            return this;
        }

        @Override
        public DiscordConfiguration build() {
            return new DiscordConfiguration(
                    token,
                    ObjectUtils.defaultIfNull(prefix, ""),
                    owner,
                    i18nPath,
                    ObjectUtils.defaultIfNull(presence, Presence.online()));
        }
    }
}

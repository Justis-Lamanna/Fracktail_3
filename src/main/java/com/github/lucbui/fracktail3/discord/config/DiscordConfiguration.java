package com.github.lucbui.fracktail3.discord.config;

import com.github.lucbui.fracktail3.discord.guards.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.platform.DiscordEventHook;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.guards.Guard;
import com.github.lucbui.fracktail3.magic.guards.channel.Channelsets;
import com.github.lucbui.fracktail3.magic.guards.user.InUsersetGuard;
import com.github.lucbui.fracktail3.magic.guards.user.Usersets;
import com.github.lucbui.fracktail3.magic.utils.model.IBuilder;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Subclass of a Configuration for a Discord bot.
 */
public class DiscordConfiguration implements Config, Localizable {
    public static final String OWNER_USERSET_ID = "owner";
    public static final Guard OWNER_GUARD = new InUsersetGuard(OWNER_USERSET_ID);

    private final String token;
    private final String prefix;
    private final Snowflake owner;
    private final StatusUpdate presence;
    private final String i18nPath;
    private final List<DiscordEventHook<?>> handlers;
    private final Usersets<DiscordUserset> usersets;
    private final Channelsets<DiscordChannelset> channelsets;

    /**
     * Initialize a bot Configuration
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     * @param owner The owner of this bot (can be null).
     * @param i18nPath The path for a Localization bundle.
     * @param presence The presence this bot should have.
     */
    public DiscordConfiguration(
            String token, String prefix, @Nullable Snowflake owner, String i18nPath,
            StatusUpdate presence, List<DiscordEventHook<?>> handlers,
            Usersets<DiscordUserset> usersets, Channelsets<DiscordChannelset> channelsets) {
        this.token = token;
        this.prefix = prefix;
        this.owner = owner;
        this.presence = presence;
        this.i18nPath = i18nPath;
        this.handlers = handlers;
        this.usersets = usersets;
        this.channelsets = channelsets;
    }

    /**
     * Initialize a bot Configuration with no owner or i18n.
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     * @param presence The presence this bot should have.
     */
    public DiscordConfiguration(String token, String prefix, StatusUpdate presence) {
        this(token, prefix, null, null, presence, Collections.emptyList(), Usersets.empty(), Channelsets.empty());
    }

    /**
     * Initialize a bot Configuration with no owner or i18n, and a general online presence.
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     */
    public DiscordConfiguration(String token, String prefix) {
        this(token, prefix, Presence.online());
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

//    @Override
//    public void configure(BotCreator creator) {
//        creator .withUserset(DiscordUserset.ALL_USERS)
//                .withUserset(DiscordUserset.NO_USERS)
//                .withChannelset(DiscordChannelset.ALL_CHANNELS)
//                .withChannelset(DiscordChannelset.NO_CHANNELS);
//        if(owner != null) {
//            creator.withUserset(DiscordUserset.forUser(OWNER_USERSET_ID, owner));
//        }
//    }

    @Override
    public ResourceBundle getBundle(Locale locale) {
        if(!isEnabled()) {
            throw new NoSuchElementException("Localization is disabled");
        }
        return ResourceBundle.getBundle(i18nPath, locale);
    }

    public List<DiscordEventHook<?>> getHandlers() {
        return Collections.unmodifiableList(handlers);
    }

    @Override
    public boolean isEnabled() {
        return i18nPath != null;
    }

    @Override
    public Optional<DiscordUserset> getUserset(String id) {
        return usersets.getById(id);
    }

    @Override
    public Optional<DiscordChannelset> getChannelset(String id) {
        return channelsets.getById(id);
    }

    public static class Builder implements IBuilder<DiscordConfiguration> {
        private final String token;
        private String prefix;
        private Snowflake owner;
        private StatusUpdate presence;
        private String i18nPath;
        private final List<DiscordEventHook<?>> handlers = new ArrayList<>();
        private final List<DiscordUserset> usersets = new ArrayList<>();
        private final List<DiscordChannelset> channelsets = new ArrayList<>();

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

        public Builder withOwner(long owner) {
            this.owner = Snowflake.of(owner);
            return this;
        }

        public Builder withPresence(StatusUpdate presence) {
            this.presence = presence;
            return this;
        }

        public Builder withHandler(DiscordEventHook<?> handler) {
            this.handlers.add(handler);
            return this;
        }

        public Builder withUserset(DiscordUserset userset) {
            this.usersets.add(userset);
            return this;
        }

        public Builder withChannelset(DiscordChannelset channelset) {
            this.channelsets.add(channelset);
            return this;
        }

        @Override
        public DiscordConfiguration build() {
            if(owner != null) {
                usersets.add(DiscordUserset.forUser("owner", owner));
            }
            usersets.add(DiscordUserset.ALL_USERS);
            usersets.add(DiscordUserset.NO_USERS);
            return new DiscordConfiguration(
                    token,
                    ObjectUtils.defaultIfNull(prefix, ""),
                    owner,
                    i18nPath,
                    ObjectUtils.defaultIfNull(presence, Presence.online()),
                    handlers,
                    new Usersets<>(usersets),
                    new Channelsets<>(channelsets));
        }
    }
}

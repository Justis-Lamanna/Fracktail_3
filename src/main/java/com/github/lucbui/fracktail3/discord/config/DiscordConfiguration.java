package com.github.lucbui.fracktail3.discord.config;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.guard.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.guard.channel.Channelsets;
import com.github.lucbui.fracktail3.magic.guard.user.InUsersetGuard;
import com.github.lucbui.fracktail3.magic.guard.user.Usersets;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Subclass of a Configuration for a Discord bot.
 */
public class DiscordConfiguration implements Config, Localizable, Validated {
    public static final String OWNER_USERSET_ID = "owner";
    public static final Guard OWNER_GUARD = new InUsersetGuard(OWNER_USERSET_ID);

    private final String token;
    private final String prefix;
    private final Snowflake owner;
    private final StatusUpdate presence;
    private final String i18nPath;
    private final ScheduledEvents scheduledEvents;
    private final List<DiscordEventHook> handlers;
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
            StatusUpdate presence, ScheduledEvents actions, List<DiscordEventHook> handlers,
            Usersets<DiscordUserset> usersets, Channelsets<DiscordChannelset> channelsets) {
        this.token = token;
        this.prefix = prefix;
        this.owner = owner;
        this.presence = presence;
        this.i18nPath = i18nPath;
        this.scheduledEvents = actions;
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
        this(token, prefix, null, null, presence, ScheduledEvents.empty(), Collections.emptyList(), Usersets.empty(), Channelsets.empty());
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

    @Override
    public ResourceBundle getBundle(Locale locale) {
        if(!isEnabled()) {
            throw new NoSuchElementException("Localization is disabled");
        }
        return ResourceBundle.getBundle(i18nPath, locale);
    }

    /**
     * Get the handlers for this configuration
     * @return The handlers used
     */
    public List<DiscordEventHook> getHandlers() {
        return Collections.unmodifiableList(handlers);
    }

    /**
     * Get the store scheduled actions
     * @return The scheduled actions
     */
    public ScheduledEvents getScheduledEvents() {
        return scheduledEvents;
    }

    /**
     * Get the store of usersets
     * @return The store of usersets
     */
    public Usersets<DiscordUserset> getUsersets() {
        return usersets;
    }

    /**
     * Get the store of channelsets
     * @return The store of channelsets
     */
    public Channelsets<DiscordChannelset> getChannelsets() {
        return channelsets;
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

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        Validated.validate(scheduledEvents, spec);
        Validated.validate(usersets, spec);
        handlers.forEach(hook -> Validated.validate(hook, spec));
        Validated.validate(channelsets, spec);
    }
}

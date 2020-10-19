package com.github.lucbui.fracktail3.discord.config;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.guard.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.magic.guard.channel.Channelsets;
import com.github.lucbui.fracktail3.magic.guard.user.Usersets;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder to construct a DiscordConfiguration
 */
public class DiscordConfigurationBuilder implements IBuilder<DiscordConfiguration> {
    private final String token;
    private String prefix;
    private Snowflake owner;
    private StatusUpdate presence;
    private String i18nPath;
    private final List<ScheduledEvent> events = new ArrayList<>();
    private final List<DiscordEventHook> handlers = new ArrayList<>();
    private final List<DiscordUserset> usersets = new ArrayList<>();
    private final List<DiscordChannelset> channelsets = new ArrayList<>();

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
     * Set the owner of this bot
     * In most cases, this is the developer.
     * Alerts will be DMed to this owner, and an "owner" userset will be created automatically.
     * @param owner The ID of the owner of the bot
     * @return This builder.
     */
    public DiscordConfigurationBuilder withOwner(Snowflake owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Set the owner of this bot
     * In most cases, this is the developer. However, this field is optional.
     * If present and "owner" userset will be created automatically.
     * @param owner The ID of the owner of the bot
     * @return This builder.
     */
    public DiscordConfigurationBuilder withOwner(long owner) {
        this.owner = Snowflake.of(owner);
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
     * Add an event hook, to be used by the PlatformRunner
     * @param handler The handler to use
     * @return This builder.
     */
    public DiscordConfigurationBuilder withHandler(DiscordEventHook handler) {
        this.handlers.add(handler);
        return this;
    }

    /**
     * Add an event hook, to be used by the PlatformRunner
     * @param handler The handler to use
     * @return This builder.
     */
    public DiscordConfigurationBuilder withHandler(IBuilder<DiscordEventHook> handler) {
        this.handlers.add(handler.build());
        return this;
    }

    /**
     * Add several event hooks, to be used by the PlatformRunner
     * @param handlers The handlers to use
     * @return This builder.
     */
    public DiscordConfigurationBuilder withHandlers(List<DiscordEventHook> handlers) {
        this.handlers.addAll(handlers);
        return this;
    }

    /**
     * Add a userset to this bot
     * @param userset The userset to use
     * @return This builder.
     */
    public DiscordConfigurationBuilder withUserset(DiscordUserset userset) {
        this.usersets.add(userset);
        return this;
    }

    /**
     * Add multiple usersets to this bot
     * @param usersets The usersets to add
     * @return This builder.
     */
    public DiscordConfigurationBuilder withUsersets(List<DiscordUserset> usersets) {
        this.usersets.addAll(usersets);
        return this;
    }

    /**
     * Add a channelset to this bot
     * @param channelset The channelset to add
     * @return This builder.
     */
    public DiscordConfigurationBuilder withChannelset(DiscordChannelset channelset) {
        this.channelsets.add(channelset);
        return this;
    }

    /**
     * Add multiple channelsets to this bot
     * @param channelsets The channelsets to add
     * @return This builder
     */
    public DiscordConfigurationBuilder withChannelsets(List<DiscordChannelset> channelsets) {
        this.channelsets.addAll(channelsets);
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

    /**
     * Add a scheduled event to execute
     * @param event The event to execute
     * @return This builder
     */
    public DiscordConfigurationBuilder withScheduledEvent(ScheduledEvent event) {
        this.events.add(event);
        return this;
    }

    /**
     * Add a scheduled event to execute
     * @param event The event to execute
     * @return This builder
     */
    public DiscordConfigurationBuilder withScheduledEvent(IBuilder<ScheduledEvent> event) {
        this.events.add(event.build());
        return this;
    }

    /**
     * Add scheduled events to execute
     * @param events The events to execute
     * @return This builder
     */
    public DiscordConfigurationBuilder withScheduledEvents(List<ScheduledEvent> events) {
        this.events.addAll(events);
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
                new ScheduledEvents(events),
                handlers,
                new Usersets<>(usersets),
                new Channelsets<>(channelsets));
    }
}

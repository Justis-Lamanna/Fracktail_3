package com.github.lucbui.fracktail3.discord.config;

import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Subclass of a Configuration for a Discord bot.
 */
@RequiredArgsConstructor
@Getter
public class DiscordConfiguration {
    private final String token;
    private final String prefix;
    private final StatusUpdate initialPresence;
    private final CommandType commandType;

    /**
     * Initialize a bot Configuration with no owner or i18n, and a general online presence.
     * @param token The Discord Token to use.
     * @param prefix The command prefix to use.
     */
    public DiscordConfiguration(String token, String prefix) {
        this(token, prefix, Presence.online(), CommandType.LEGACY);
    }

    /**
     * Initialize a bot with just a token.
     * @param token The Discord Token
     */
    public DiscordConfiguration(String token) { this(token, ""); }
}

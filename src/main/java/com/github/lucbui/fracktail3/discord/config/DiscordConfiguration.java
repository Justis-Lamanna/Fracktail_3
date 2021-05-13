package com.github.lucbui.fracktail3.discord.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import com.github.lucbui.fracktail3.discord.platform.DiscordHook;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

/**
 * Subclass of a Configuration for a Discord bot.
 */
@Getter
@Builder
public class DiscordConfiguration {
    @JsonIgnore
    private final String token;
    private final String prefix;
    @Builder.Default private final StatusUpdate initialPresence = Presence.online();
    @Builder.Default private final CommandType commandType = CommandType.LEGACY;
    @Builder.Default private final ReplyStyle replyStyle = ReplyStyle.PLAIN;
    @Singular private final List<DiscordHook> hooks;
}

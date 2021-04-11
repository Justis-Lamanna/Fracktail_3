package com.github.lucbui.fracktail3.discord.context;

import discord4j.core.object.entity.User;

/**
 * A wrapper around a bot user
 * Identical to a DiscordPerson, but allows for testing that it's a bot
 */
public class DiscordBotPerson extends DiscordPerson {
    public DiscordBotPerson(User user) {
        super(user);
    }
}

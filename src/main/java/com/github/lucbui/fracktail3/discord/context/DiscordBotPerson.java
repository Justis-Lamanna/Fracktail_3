package com.github.lucbui.fracktail3.discord.context;

import discord4j.core.object.entity.User;

public class DiscordBotPerson extends DiscordPerson {
    public DiscordBotPerson(User user) {
        super(user);
    }
}

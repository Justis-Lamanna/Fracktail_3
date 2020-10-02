package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfig {
    @Bean
    DiscordPlatform discordPlatform(@Value("${token}") String token) {
        return new DiscordPlatform(new DiscordConfiguration.Builder(token)
                .withPrefix("!")
                .withOwner(248612704019808258L)
                .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                .withUserset(DiscordUserset.forUser("steven", 112005555178000384L))
                .build());
    }
}

package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.event.events.DiscordSupportedEvent;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class DiscordBotConfig {
    @Bean
    DiscordPlatform discordPlatform(@Value("${token}") String token) {
        return new DiscordPlatform(new DiscordConfigurationBuilder(token)
                .withPrefix("!")
                .withOwner(248612704019808258L)
                .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                .withUserset(DiscordUserset.forUser("steven", 112005555178000384L))
                .withHandler(new DiscordEventHook.Builder<>("test", DiscordSupportedEvent.forEvent(GuildCreateEvent.class))
                        .setHandler((bot, ctx) -> {
                            System.out.println("Joined " + ctx.getEvent().getRawEvent().getGuild().getName());
                            return Mono.empty();
                        })
                        .build())
                .build());
    }
}

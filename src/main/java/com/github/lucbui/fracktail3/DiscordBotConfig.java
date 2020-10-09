package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHandler;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.schedule.ExecuteAfterDurationTrigger;
import com.github.lucbui.fracktail3.magic.schedule.ScheduleContext;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledAction;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
@EnableScheduling
public class DiscordBotConfig {
    @Bean
    DiscordPlatform discordPlatform(@Value("${token}") String token) {
        return new DiscordPlatform(new DiscordConfigurationBuilder(token)
                .withPrefix("!")
                .withOwner(248612704019808258L)
                .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                .withUserset(DiscordUserset.forUser("steven", 112005555178000384L))
                .withHandler(new DiscordEventHook.Builder("test")
                        .setHandler(new DiscordEventHandler() {
                            @Override
                            public Mono<Void> onGuildCreate(Bot bot, DiscordConfiguration config, GuildCreateEvent event) {
                                System.out.println("Joined " + event.getGuild().getName());
                                return Mono.empty();
                            }
                        })
                )
                .withScheduledEvent(new ScheduledEvent(
                        "test",
                        new ExecuteAfterDurationTrigger(Duration.ofSeconds(30)),
                        new ScheduledAction() {
                            @Override
                            public Mono<Void> execute(Bot bot, ScheduleContext context) {
                                System.out.println("Executing, but only once.");
                                context.getScheduledEvent().cancel();
                                return Mono.empty();
                            }
                        }))
                .build());
    }
}

package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.BasicParameterParser;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    public Platform discord(@Value("${token}") String token, List<ReactiveEventAdapter> hooks) {
        DiscordConfiguration configuration = DiscordConfiguration.builder()
                .token(token)
                .prefix("!")
                .initialPresence(Presence.doNotDisturb(Activity.watching("you be such a cutie")))
                .replyStyle(ReplyStyle.PLAIN)
                .hooks(hooks)
                .build();

        return new DiscordPlatform(
                configuration,
                new BasicParameterParser()
        );
    }
}

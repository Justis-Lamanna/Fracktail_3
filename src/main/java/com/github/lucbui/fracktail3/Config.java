package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import com.github.lucbui.fracktail3.discord.platform.Activity;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.platform.Presence;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.BasicParameterParser;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    public StatusUpdate statusUpdate(
            @Value("${discord.status.presence:ONLINE}") Presence presence,
            @Value("${discord.status.activity:NONE}") Activity activity,
            @Value("${discord.status.string:}") String string,
            @Value("${discord.status.url:}") String url) {
        return presence.create(activity, string, url);
    }

    @Bean
    public Platform discord(@Value("${discord.token}") String token, @Value("${discord.prefix:!}") String prefix,
                            StatusUpdate statusUpdate, List<ReactiveEventAdapter> hooks) {
        DiscordConfiguration configuration = DiscordConfiguration.builder()
                .token(token)
                .prefix(prefix)
                .initialPresence(statusUpdate)
                .replyStyle(ReplyStyle.REPLY)
                .hooks(hooks)
                .build();

        return new DiscordPlatform(
                configuration,
                new BasicParameterParser()
        );
    }
}

package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    public DiscordConfiguration discord(@Value("${discord.token}") String token, @Value("${discord.prefix:!}") String prefix,
                                        @Autowired(required = false) StatusUpdate statusUpdate, List<ReactiveEventAdapter> hooks) {
        return DiscordConfiguration.builder()
                .token(token)
                .prefix(prefix)
                .initialPresence(ObjectUtils.defaultIfNull(statusUpdate, Presence.online()))
                .replyStyle(ReplyStyle.REPLY)
                .hooks(hooks)
                .build();
    }
}

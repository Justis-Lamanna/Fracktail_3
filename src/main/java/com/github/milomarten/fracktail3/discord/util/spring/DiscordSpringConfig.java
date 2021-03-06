package com.github.milomarten.fracktail3.discord.util.spring;

import com.github.milomarten.fracktail3.discord.config.DiscordConfiguration;
import com.github.milomarten.fracktail3.discord.context.ReplyStyle;
import com.github.milomarten.fracktail3.discord.platform.DiscordHook;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.object.presence.Presence;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DiscordSpringConfig {
    @Bean
    @ConditionalOnMissingBean
    public DiscordConfiguration discord(@Value("${discord.token}") String token, @Value("${discord.prefix:!}") String prefix,
                                        @Autowired(required = false) StatusUpdate statusUpdate, Map<String, ReactiveEventAdapter> hooks) {
        return DiscordConfiguration.builder()
                .token(token)
                .prefix(prefix)
                .initialPresence(ObjectUtils.defaultIfNull(statusUpdate, Presence.online()))
                .replyStyle(ReplyStyle.REPLY)
                .hooks(DiscordHook.fromMap(hooks))
                .build();
    }
}

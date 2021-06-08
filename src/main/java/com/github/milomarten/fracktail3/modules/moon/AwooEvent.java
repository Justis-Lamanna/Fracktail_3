package com.github.milomarten.fracktail3.modules.moon;

import com.github.milomarten.fracktail3.discord.platform.DiscordPlatform;
import com.github.milomarten.fracktail3.discord.util.FormatUtils;
import com.github.milomarten.fracktail3.magic.schedule.ScheduledEvent;
import discord4j.common.util.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class AwooEvent {
    private static final String AT_WOLVES = FormatUtils.mentionRole(Snowflake.of(560501222583566336L));

    @Bean
    public ScheduledEvent awooScheduledEvent() {
        return new ScheduledEvent("awoo", new FullMoonScheduleEventTrigger(), ctx -> ctx.getBot().getPlatform(DiscordPlatform.class)
                .map(platform -> platform.getPlace("channel:746898862098087977")
                    .flatMap(p -> p.sendMessage(AT_WOLVES + " \uD83C\uDF15"))
                    .then())
                .orElse(Mono.empty()));
    }
}
